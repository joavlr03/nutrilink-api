package br.com.joavlr03.nutrilink_api.dto.sincronizacao;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public class SincronizacaoCreateRequest {

    @NotNull(message = "ID da coleta é obrigatório")
    private UUID coletaId;

    public UUID getColetaId() { return coletaId; }
    public void setColetaId(UUID coletaId) { this.coletaId = coletaId; }
}
