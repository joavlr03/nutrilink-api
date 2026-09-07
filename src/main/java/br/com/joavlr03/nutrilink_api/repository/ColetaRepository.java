package br.com.joavlr03.nutrilink_api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.joavlr03.nutrilink_api.model.Coleta;
import br.com.joavlr03.nutrilink_api.model.enums.StatusColeta;

public interface ColetaRepository extends JpaRepository<Coleta, UUID> {

    List<Coleta> findByDoadoraId(UUID doadoraId);

    List<Coleta> findByCorredorId(UUID corredorId);

    List<Coleta> findByStatusColeta(StatusColeta statusColeta);
}
