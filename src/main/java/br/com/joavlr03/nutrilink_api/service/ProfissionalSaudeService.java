package br.com.joavlr03.nutrilink_api.service;

import br.com.joavlr03.nutrilink_api.model.ProfissionalSaude;
import br.com.joavlr03.nutrilink_api.model.enums.TipoProfissional;
import br.com.joavlr03.nutrilink_api.repository.ProfissionalSaudeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProfissionalSaudeService {

    private final ProfissionalSaudeRepository repository;

    public ProfissionalSaudeService(ProfissionalSaudeRepository repository) {
        this.repository = repository;
    }

    public ProfissionalSaude create(ProfissionalSaude profissional) {
        if (repository.existsByRegistroConselho(profissional.getRegistroConselho())) {
            throw new IllegalArgumentException(
                "Registro de conselho já cadastrado: " + profissional.getRegistroConselho()
            );
        }
        profissional.setCredencialAtiva(true);
        return repository.save(profissional);
    }

    public Optional<ProfissionalSaude> findById(UUID id) {
        return repository.findById(id);
    }

    public List<ProfissionalSaude> findAll() {
        return repository.findAll();
    }

    public List<ProfissionalSaude> findByTipo(TipoProfissional tipo) {
        return repository.findByTipoProfissional(tipo);
    }

    public List<ProfissionalSaude> findAtivos() {
        return repository.findByCredencialAtivaTrue();
    }

    public ProfissionalSaude inativar(UUID id) {
        ProfissionalSaude profissional = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado: " + id));
        profissional.setCredencialAtiva(false);
        return repository.save(profissional);
    }

    public ProfissionalSaude reativar(UUID id) {
        ProfissionalSaude profissional = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado: " + id));
        profissional.setCredencialAtiva(true);
        return repository.save(profissional);
    }

    public void deleteById(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Profissional não encontrado: " + id);
        }
        repository.deleteById(id);
    }
}