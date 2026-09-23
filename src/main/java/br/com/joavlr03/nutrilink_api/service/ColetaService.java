package br.com.joavlr03.nutrilink_api.service;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * Transições permitidas:
     *   AGENDADA -> EM_ROTA | CANCELADA
     *   EM_ROTA  -> CONCLUIDA | CANCELADA
     *   CONCLUIDA e CANCELADA são estados finais.
     */
    private static final Map<StatusColeta, Set<StatusColeta>> TRANSICOES = new EnumMap<>(StatusColeta.class);
    static {
        TRANSICOES.put(StatusColeta.AGENDADA,  EnumSet.of(StatusColeta.EM_ROTA, StatusColeta.CANCELADA));
        TRANSICOES.put(StatusColeta.EM_ROTA,   EnumSet.of(StatusColeta.CONCLUIDA, StatusColeta.CANCELADA));
        TRANSICOES.put(StatusColeta.CONCLUIDA, EnumSet.noneOf(StatusColeta.class));
        TRANSICOES.put(StatusColeta.CANCELADA, EnumSet.noneOf(StatusColeta.class));
    }

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

    @Transactional
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

    @Transactional
    public Coleta atualizarStatus(UUID id, StatusColeta novoStatus) {
        Coleta coleta = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Coleta não encontrada: " + id));

        StatusColeta atual = coleta.getStatusColeta();
        Set<StatusColeta> permitidos = TRANSICOES.getOrDefault(atual, EnumSet.noneOf(StatusColeta.class));

        if (!permitidos.contains(novoStatus)) {
            String opcoes = permitidos.isEmpty() ? "nenhuma (status final)" : permitidos.toString();
            throw new IllegalStateException(
                "Transição inválida: " + atual + " -> " + novoStatus + ". Transições permitidas: " + opcoes + ".");
        }

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
