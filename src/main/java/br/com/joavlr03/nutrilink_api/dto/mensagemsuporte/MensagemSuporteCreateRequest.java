package br.com.joavlr03.nutrilink_api.dto.mensagemsuporte;

import java.util.UUID;

import br.com.joavlr03.nutrilink_api.model.enums.RemetenteTipo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MensagemSuporteCreateRequest {
    @NotNull(message = "ID do ticket é obrigatório")
    private UUID ticketId;

    @NotNull(message = "Tipo do remetente é obrigatório")
    private RemetenteTipo remetenteTipo;

    @NotNull(message = "ID do remetente é obrigatório")
    private UUID remetenteId;

    @NotBlank(message = "Conteúdo da mensagem é obrigatório")
    @Size(max = 1000, message = "Mensagem deve ter no máximo 1000 caracteres")
    private String conteudoMensagem;

    public UUID getTicketId() {
        return ticketId;
    }

    public void setTicketId(UUID ticketId) {
        this.ticketId = ticketId;
    }

    public RemetenteTipo getRemetenteTipo() {
        return remetenteTipo;
    }

    public void setRemetenteTipo(RemetenteTipo remetenteTipo) {
        this.remetenteTipo = remetenteTipo;
    }

    public UUID getRemetenteId() {
        return remetenteId;
    }

    public void setRemetenteId(UUID remetenteId) {
        this.remetenteId = remetenteId;
    }

    public String getConteudoMensagem() {
        return conteudoMensagem;
    }

    public void setConteudoMensagem(String conteudoMensagem) {
        this.conteudoMensagem = conteudoMensagem;
    }
}
