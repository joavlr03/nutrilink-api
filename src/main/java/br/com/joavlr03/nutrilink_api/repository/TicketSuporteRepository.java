package br.com.joavlr03.nutrilink_api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.joavlr03.nutrilink_api.model.TicketSuporte;
import br.com.joavlr03.nutrilink_api.model.enums.StatusTicket;

public interface TicketSuporteRepository extends JpaRepository<TicketSuporte, UUID> {

    List<TicketSuporte> findByDoadoraId(UUID doadoraId);

    List<TicketSuporte> findByProfissionalId(UUID profissionalId);

    List<TicketSuporte> findByStatusTicket(StatusTicket statusTicket);
}
