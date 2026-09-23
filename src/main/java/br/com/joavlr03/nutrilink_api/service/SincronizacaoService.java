package br.com.joavlr03.nutrilink_api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.joavlr03.nutrilink_api.model.Coleta;
import br.com.joavlr03.nutrilink_api.model.Sincronizacao;
import br.com.joavlr03.nutrilink_api.model.enums.StatusColeta;
import br.com.joavlr03.nutrilink_api.model.enums.StatusSincronizacao;
import br.com.joavlr03.nutrilink_api.repository.ColetaRepository;
import br.com.joavlr03.nutrilink_api.repository.SincronizacaoRepository;
import jakarta.persistence.EntityNotFoundException;

/**
 * Registra o envio de uma coleta a um sistema externo (ex.: sistema do banco de leite/hospital).
 *
 * Ciclo de vida:  PENDENTE --confirmar--> SUCESSO
 *                 PENDENTE --falha-----> FALHA --reprocessar--> PENDENTE
 */
@Service
public class SincronizacaoService {

    private final SincronizacaoRepository repository;
    private final ColetaRepository coletaRepository;

    public SincronizacaoService(SincronizacaoRepository repository, ColetaRepository coletaRepository) {
        this.repository = repository;
        this.coletaRepository = coletaRepository;
    }

    @Transactional
    public Sincronizacao create(UUID coletaId) {
        Coleta coleta = coletaRepository.findById(coletaId)
                .orElseThrow(() -> new EntityNotFoundException("Coleta não encontrada: " + coletaId));

        if (coleta.getStatusColeta() == StatusColeta.CANCELADA) {
            throw new IllegalStateException("Não é possível sincronizar uma coleta cancelada.");
        }

        if (repository.existsByColetaId(coletaId)) {
            throw new IllegalStateException("A coleta " + coletaId + " já possui uma sincronização registrada.");
        }

        Sincronizacao sincronizacao = new Sincronizacao();
        sincronizacao.setColeta(coleta);
        sincronizacao.setPayloadEnviado(montarPayload(coleta));
        sincronizacao.setStatusSincronizacao(StatusSincronizacao.PENDENTE);

        return repository.save(sincronizacao);
    }

    public Optional<Sincronizacao> findById(UUID id) {
        return repository.findById(id);
    }

    public List<Sincronizacao> findAll() {
        return repository.findAll();
    }

    public Sincronizacao findByColetaId(UUID coletaId) {
        return repository.findByColetaId(coletaId)
                .orElseThrow(() -> new EntityNotFoundException("Nenhuma sincronização para a coleta: " + coletaId));
    }

    public List<Sincronizacao> findByStatus(StatusSincronizacao status) {
        return repository.findByStatusSincronizacao(status);
    }

    @Transactional
    public Sincronizacao confirmar(UUID id, String protocoloGerado) {
        Sincronizacao sincronizacao = buscarOuFalhar(id);
        exigirStatus(sincronizacao, StatusSincronizacao.PENDENTE, "confirmar");

        sincronizacao.setProtocoloGerado(protocoloGerado);
        sincronizacao.setStatusSincronizacao(StatusSincronizacao.SUCESSO);
        sincronizacao.setDataSincronizacao(LocalDateTime.now());
        return repository.save(sincronizacao);
    }

    @Transactional
    public Sincronizacao registrarFalha(UUID id) {
        Sincronizacao sincronizacao = buscarOuFalhar(id);
        exigirStatus(sincronizacao, StatusSincronizacao.PENDENTE, "registrar falha em");

        sincronizacao.setStatusSincronizacao(StatusSincronizacao.FALHA);
        sincronizacao.setDataSincronizacao(LocalDateTime.now());
        return repository.save(sincronizacao);
    }

    @Transactional
    public Sincronizacao reprocessar(UUID id) {
        Sincronizacao sincronizacao = buscarOuFalhar(id);
        exigirStatus(sincronizacao, StatusSincronizacao.FALHA, "reprocessar");

        // Reenvia com os dados atuais da coleta
        sincronizacao.setPayloadEnviado(montarPayload(sincronizacao.getColeta()));
        sincronizacao.setProtocoloGerado(null);
        sincronizacao.setDataSincronizacao(null);
        sincronizacao.setStatusSincronizacao(StatusSincronizacao.PENDENTE);
        return repository.save(sincronizacao);
    }

    @Transactional
    public void deleteById(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Sincronização não encontrada: " + id);
        }
        repository.deleteById(id);
    }

    // --- Métodos internos ---

    private Sincronizacao buscarOuFalhar(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sincronização não encontrada: " + id));
    }

    private void exigirStatus(Sincronizacao sincronizacao, StatusSincronizacao esperado, String acao) {
        if (sincronizacao.getStatusSincronizacao() != esperado) {
            throw new IllegalStateException(
                "Não é possível " + acao + " uma sincronização com status "
                + sincronizacao.getStatusSincronizacao() + ". Status exigido: " + esperado + ".");
        }
    }

    /** Snapshot da coleta no momento do envio (somente UUIDs, datas, números e enums, então não precisa de escape). */
    private String montarPayload(Coleta coleta) {
        return String.format(
            "{\"coletaId\":\"%s\",\"doadoraId\":\"%s\",\"corredorId\":\"%s\","
            + "\"dataAgendada\":\"%s\",\"volumeEstimadoMl\":%s,\"statusColeta\":\"%s\"}",
            coleta.getId(),
            coleta.getDoadora().getId(),
            coleta.getCorredor().getId(),
            coleta.getDataAgendada(),
            coleta.getVolumeEstimadoMl(),   // vira null no JSON se não informado
            coleta.getStatusColeta());
    }
}
