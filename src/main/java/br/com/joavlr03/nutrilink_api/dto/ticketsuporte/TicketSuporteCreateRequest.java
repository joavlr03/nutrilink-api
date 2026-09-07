package br.com.joavlr03.nutrilink_api.dto.ticketsuporte;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TicketSuporteCreateRequest {
     @NotNull(message = "ID da doadora é obrigatório")
    private UUID doadoraId;

    @NotBlank(message = "Assunto é obrigatório")
    @Size(max = 100, message = "Assunto deve ter no máximo 100 caracteres")
    private String assunto;

    public UUID getDoadoraId() { return doadoraId; }
    public void setDoadoraId(UUID doadoraId) { this.doadoraId = doadoraId; }

    public String getAssunto() { return assunto; }
    public void setAssunto(String assunto) { this.assunto = assunto; }
}
