package br.com.joavlr03.nutrilink_api.dto.mensagemsuporte;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.joavlr03.nutrilink_api.model.enums.RemetenteTipo;

public class MensagemSuporteResponse {
    private UUID id;
    private UUID ticketId;
    private RemetenteTipo remetenteTipo;
    private UUID remetenteId;
    private String conteudoMensagem;
    private LocalDateTime dataEnvio;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getTicketId() { return ticketId; }
    public void setTicketId(UUID ticketId) { this.ticketId = ticketId; }

    public RemetenteTipo getRemetenteTipo() { return remetenteTipo; }
    public void setRemetenteTipo(RemetenteTipo remetenteTipo) { this.remetenteTipo = remetenteTipo; }

    public UUID getRemetenteId() { return remetenteId; }
    public void setRemetenteId(UUID remetenteId) { this.remetenteId = remetenteId; }

    public String getConteudoMensagem() { return conteudoMensagem; }
    public void setConteudoMensagem(String conteudoMensagem) { this.conteudoMensagem = conteudoMensagem; }

    public LocalDateTime getDataEnvio() { return dataEnvio; }
    public void setDataEnvio(LocalDateTime dataEnvio) { this.dataEnvio = dataEnvio; }
}

