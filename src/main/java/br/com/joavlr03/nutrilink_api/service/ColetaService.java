package br.com.joavlr03.nutrilink_api.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.joavlr03.nutrilink_api.model.Coleta;
import br.com.joavlr03.nutrilink_api.model.CorredorLogistico;
import br.com.joavlr03.nutrilink_api.model.Doadora;
import br.com.joavlr03.nutrilink_api.model.enums.StatusCadastro;
import br.com.joavlr03.nutrilink_api.model.enums.StatusColeta;
import br.com.joavlr03.nutrilink_api.repository.ColetaRepository;
import br.com.joavlr03.nutrilink_api.repository.CorredorLogisticoRepository;
import br.com.joavlr03.nutrilink_api.repository.DoadoraRepository;
import jakarta.persistence.EntityNotFoundException;

@Service 
public class ColetaService {
     private final ColetaRepository repository;
    private final DoadoraRepository doadoraRepository;
    private final CorredorLogisticoRepository corredorRepository;

    public ColetaService(
            ColetaRepository repository,
            DoadoraRepository doadoraRepository,
            CorredorLogisticoRepository corredorRepository) {
        this.repository = repository;
        this.doadoraRepository = doadoraRepository;
        this.corredorRepository = corredorRepository;
    }

    public Coleta create(Coleta coleta, UUID doadoraId, UUID corredorId) {

        // 1. Resolve doadora
        Doadora doadora = doadoraRepository.findById(doadoraId)
                .orElseThrow(() -> new EntityNotFoundException("Doadora não encontrada: " + doadoraId));

        // 2. Valida se doadora está aprovada
        if (doadora.getStatusCadastro() != StatusCadastro.APROVADA) {
            throw new IllegalStateException("Doadora não está aprovada para agendamento.");
        }

        // 3. Resolve corredor
        CorredorLogistico corredor = corredorRepository.findById(corredorId)
                .orElseThrow(() -> new EntityNotFoundException("Corredor não encontrado: " + corredorId));

        // 4. Valida se corredor está homologado
        if (!corredor.getStatusHomologacao()) {
            throw new IllegalStateException("Corredor logístico desabilitado: " + corredorId);
        }

        // 5. Valida se CEP da doadora pertence ao corredor
        String prefixoCep = doadora.getCep().substring(0, 3);
        if (!corredor.getCepsAtendidos().contains(prefixoCep)) {
            throw new IllegalArgumentException(
                "CEP da doadora não pertence ao corredor informado."
            );
        }

        coleta.setDoadora(doadora);
        coleta.setCorredor(corredor);
        coleta.setStatusColeta(StatusColeta.AGENDADA);

        return repository.save(coleta);
    }

    public Optional<Coleta> findById(UUID id) {
        return repository.findById(id);
    }

    public List<Coleta> findAll() {
        return repository.findAll();
    }

    public List<Coleta> findByDoadoraId(UUID doadoraId) {
        return repository.findByDoadoraId(doadoraId);
    }

    public List<Coleta> findByStatus(StatusColeta status) {
        return repository.findByStatusColeta(status);
    }

    public Coleta atualizarStatus(UUID id, StatusColeta novoStatus) {
        Coleta coleta = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Coleta não encontrada: " + id));
        coleta.setStatusColeta(novoStatus);
        return repository.save(coleta);
    }

    public void deleteById(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Coleta não encontrada: " + id);
        }
        repository.deleteById(id);
    }
}
