package br.com.joavlr03.nutrilink_api.dto.sincronizacao;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.joavlr03.nutrilink_api.model.enums.StatusSincronizacao;

public class SincronizacaoResponse {
    private UUID id;
    private UUID coletaId;
    private String payloadEnviado;
    private String protocoloGerado;
    private StatusSincronizacao statusSincronizacao;
    private LocalDateTime dataSincronizacao;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getColetaId() { return coletaId; }
    public void setColetaId(UUID coletaId) { this.coletaId = coletaId; }

    public String getPayloadEnviado() { return payloadEnviado; }
    public void setPayloadEnviado(String payloadEnviado) { this.payloadEnviado = payloadEnviado; }

    public String getProtocoloGerado() { return protocoloGerado; }
    public void setProtocoloGerado(String protocoloGerado) { this.protocoloGerado = protocoloGerado; }

    public StatusSincronizacao getStatusSincronizacao() { return statusSincronizacao; }
    public void setStatusSincronizacao(StatusSincronizacao statusSincronizacao) { this.statusSincronizacao = statusSincronizacao; }

    public LocalDateTime getDataSincronizacao() { return dataSincronizacao; }
    public void setDataSincronizacao(LocalDateTime dataSincronizacao) { this.dataSincronizacao = dataSincronizacao; }
}
