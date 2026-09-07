package br.com.joavlr03.nutrilink_api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.joavlr03.nutrilink_api.model.CorredorLogistico;

public interface CorredorLogisticoRepository extends JpaRepository<CorredorLogistico, UUID> {

    List<CorredorLogistico> findByStatusHomologacaoTrue();
}
