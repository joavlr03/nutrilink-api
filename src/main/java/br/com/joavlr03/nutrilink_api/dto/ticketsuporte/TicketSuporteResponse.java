package br.com.joavlr03.nutrilink_api.dto.ticketsuporte;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.joavlr03.nutrilink_api.model.enums.StatusTicket;

public class TicketSuporteResponse {
    private UUID id;
    private UUID doadoraId;
    private UUID profissionalId;
    private String assunto;
    private StatusTicket statusTicket;
    private LocalDateTime dataAbertura;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getDoadoraId() { return doadoraId; }
    public void setDoadoraId(UUID doadoraId) { this.doadoraId = doadoraId; }

    public UUID getProfissionalId() { return profissionalId; }
    public void setProfissionalId(UUID profissionalId) { this.profissionalId = profissionalId; }

    public String getAssunto() { return assunto; }
    public void setAssunto(String assunto) { this.assunto = assunto; }

    public StatusTicket getStatusTicket() { return statusTicket; }
    public void setStatusTicket(StatusTicket statusTicket) { this.statusTicket = statusTicket; }

    public LocalDateTime getDataAbertura() { return dataAbertura; }
    public void setDataAbertura(LocalDateTime dataAbertura) { this.dataAbertura = dataAbertura; }
}
