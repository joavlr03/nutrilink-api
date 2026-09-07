package br.com.joavlr03.nutrilink_api.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.joavlr03.nutrilink_api.model.CorredorLogistico;
import br.com.joavlr03.nutrilink_api.repository.CorredorLogisticoRepository;
import jakarta.persistence.EntityNotFoundException;

@Service 
public class CorredorLogisticoService {
     private final CorredorLogisticoRepository repository;

    public CorredorLogisticoService(CorredorLogisticoRepository repository) {
        this.repository = repository;
    }

    public CorredorLogistico create(CorredorLogistico corredor) {
        corredor.setStatusHomologacao(true);
        return repository.save(corredor);
    }

    public Optional<CorredorLogistico> findById(UUID id) {
        return repository.findById(id);
    }

    public List<CorredorLogistico> findAll() {
        return repository.findAll();
    }

    public List<CorredorLogistico> findAtivos() {
        return repository.findByStatusHomologacaoTrue();
    }

    public CorredorLogistico desabilitar(UUID id) {
        CorredorLogistico corredor = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Corredor não encontrado: " + id));
        corredor.setStatusHomologacao(false);
        return repository.save(corredor);
    }

    public CorredorLogistico habilitar(UUID id) {
        CorredorLogistico corredor = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Corredor não encontrado: " + id));
        corredor.setStatusHomologacao(true);
        return repository.save(corredor);
    }

    public boolean validarCepNoCorredor(UUID corredorId, String cep) {
        CorredorLogistico corredor = repository.findById(corredorId)
                .orElseThrow(() -> new EntityNotFoundException("Corredor não encontrado: " + corredorId));

        if (!corredor.getStatusHomologacao()) {
            throw new IllegalStateException("Corredor desabilitado: " + corredorId);
        }

        String prefixoCep = cep.substring(0, 3);
        return corredor.getCepsAtendidos().contains(prefixoCep);
    }

    public void deleteById(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Corredor não encontrado: " + id);
        }
        repository.deleteById(id);
    }
}
