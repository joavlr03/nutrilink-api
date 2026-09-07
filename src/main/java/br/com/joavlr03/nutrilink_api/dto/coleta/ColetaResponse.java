package br.com.joavlr03.nutrilink_api.dto.coleta;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.joavlr03.nutrilink_api.model.enums.StatusColeta;

public class ColetaResponse {
    private UUID id;
    private UUID doadoraId;
    private UUID corredorId;
    private LocalDateTime dataAgendada;
    private Integer volumeEstimadoMl;
    private StatusColeta statusColeta;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getDoadoraId() { return doadoraId; }
    public void setDoadoraId(UUID doadoraId) { this.doadoraId = doadoraId; }

    public UUID getCorredorId() { return corredorId; }
    public void setCorredorId(UUID corredorId) { this.corredorId = corredorId; }

    public LocalDateTime getDataAgendada() { return dataAgendada; }
    public void setDataAgendada(LocalDateTime dataAgendada) { this.dataAgendada = dataAgendada; }

    public Integer getVolumeEstimadoMl() { return volumeEstimadoMl; }
    public void setVolumeEstimadoMl(Integer volumeEstimadoMl) { this.volumeEstimadoMl = volumeEstimadoMl; }

    public StatusColeta getStatusColeta() { return statusColeta; }
    public void setStatusColeta(StatusColeta statusColeta) { this.statusColeta = statusColeta; }
}
