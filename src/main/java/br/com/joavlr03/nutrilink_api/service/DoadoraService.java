package br.com.joavlr03.nutrilink_api.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.joavlr03.nutrilink_api.model.Doadora;
import br.com.joavlr03.nutrilink_api.model.enums.StatusCadastro;
import br.com.joavlr03.nutrilink_api.repository.DoadoraRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class DoadoraService {
    @Autowired
    private DoadoraRepository repository;

    public Doadora create(Doadora doadora) {
        if (repository.existsByCpf(doadora.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado: " + doadora.getCpf());
        }

        validarMaioridade(doadora.getDataNascimento());

        doadora.setStatusCadastro(StatusCadastro.PENDENTE);

        return repository.save(doadora);
    }

    /** Atualiza os dados cadastrais. CPF e statusCadastro são preservados. */
    @Transactional
    public Doadora update(UUID id, Doadora dados) {
        Doadora doadora = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doadora não encontrada: " + id));

        validarMaioridade(dados.getDataNascimento());

        doadora.setNomeCompleto(dados.getNomeCompleto());
        doadora.setDataNascimento(dados.getDataNascimento());
        doadora.setTelefone(dados.getTelefone());
        doadora.setCep(dados.getCep());
        doadora.setEnderecoCompleto(dados.getEnderecoCompleto());

        return repository.save(doadora);
    }

    public Optional<Doadora> findById(UUID id) {
        return repository.findById(id);
    }

    public List<Doadora> findAll() {
        return repository.findAll();
    }

    public void deleteById(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Doadora não encontrada: " + id);
        }
        repository.deleteById(id);
    }

    public Doadora save(Doadora doadora) {
        return repository.save(doadora);
    }

    private void validarMaioridade(LocalDate dataNascimento) {
        int idade = Period.between(dataNascimento, LocalDate.now()).getYears();
        if (idade < 18) {
            throw new IllegalArgumentException("Doadora deve ser maior de idade.");
        }
    }
}
