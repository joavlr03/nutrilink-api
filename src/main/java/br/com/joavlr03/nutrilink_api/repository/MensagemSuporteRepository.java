package br.com.joavlr03.nutrilink_api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.joavlr03.nutrilink_api.model.MensagemSuporte;
import br.com.joavlr03.nutrilink_api.model.enums.RemetenteTipo;

public interface MensagemSuporteRepository extends JpaRepository<MensagemSuporte, UUID> {

    List<MensagemSuporte> findByTicketId(UUID ticketId);

    List<MensagemSuporte> findByTicketIdOrderByDataEnvioAsc(UUID ticketId);

    List<MensagemSuporte> findByRemetenteTipo(RemetenteTipo remetenteTipo);
}
