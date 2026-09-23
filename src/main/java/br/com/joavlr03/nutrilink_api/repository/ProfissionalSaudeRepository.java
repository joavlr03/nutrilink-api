package br.com.joavlr03.nutrilink_api.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.joavlr03.nutrilink_api.model.ProfissionalSaude;
import br.com.joavlr03.nutrilink_api.model.enums.TipoProfissional;

public interface ProfissionalSaudeRepository extends JpaRepository<ProfissionalSaude, UUID> {

    Optional<ProfissionalSaude> findByRegistroConselho(String registroConselho);

    boolean existsByRegistroConselho(String registroConselho);

    /** Usado na atualização: o registro não pode pertencer a OUTRO profissional. */
    boolean existsByRegistroConselhoAndIdNot(String registroConselho, UUID id);

    List<ProfissionalSaude> findByTipoProfissional(TipoProfissional tipoProfissional);

    List<ProfissionalSaude> findByCredencialAtivaTrue();
}
