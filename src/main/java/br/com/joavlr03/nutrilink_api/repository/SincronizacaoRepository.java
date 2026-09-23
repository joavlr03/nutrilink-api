package br.com.joavlr03.nutrilink_api.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.joavlr03.nutrilink_api.model.Sincronizacao;
import br.com.joavlr03.nutrilink_api.model.enums.StatusSincronizacao;

public interface SincronizacaoRepository extends JpaRepository<Sincronizacao, UUID> {

    Optional<Sincronizacao> findByColetaId(UUID coletaId);

    boolean existsByColetaId(UUID coletaId);

    List<Sincronizacao> findByStatusSincronizacao(StatusSincronizacao statusSincronizacao);
}
