package br.com.joavlr03.nutrilink_api.dto.sincronizacao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SincronizacaoConfirmacaoRequest {

    @NotBlank(message = "Protocolo gerado pelo sistema externo é obrigatório")
    @Size(max = 100, message = "Protocolo deve ter no máximo 100 caracteres")
    private String protocoloGerado;

    public String getProtocoloGerado() { return protocoloGerado; }
    public void setProtocoloGerado(String protocoloGerado) { this.protocoloGerado = protocoloGerado; }
}
