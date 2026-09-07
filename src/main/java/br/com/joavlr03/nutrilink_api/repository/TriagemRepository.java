package br.com.joavlr03.nutrilink_api.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.joavlr03.nutrilink_api.model.Triagem;
import br.com.joavlr03.nutrilink_api.model.enums.StatusTriagem;

public interface TriagemRepository extends JpaRepository<Triagem, UUID> {

    List<Triagem> findByDoadoraId(UUID doadoraId);

    List<Triagem> findByStatusTriagem(StatusTriagem statusTriagem);

    List<Triagem> findByRequerValidacaoHumanaTrue();

    Optional<Triagem> findById(UUID id);

    List<Triagem> findAll();

    boolean existsById(UUID id);

    void deleteById(UUID id);
}
