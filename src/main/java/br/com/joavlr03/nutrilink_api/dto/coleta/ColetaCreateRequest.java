package br.com.joavlr03.nutrilink_api.dto.coleta;

import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public class ColetaCreateRequest {
    @NotNull(message = "ID da doadora é obrigatório")
    private UUID doadoraId;

    @NotNull(message = "ID do corredor é obrigatório")
    private UUID corredorId;

    @NotNull(message = "Data agendada é obrigatória")
    @Future(message = "A data agendada deve ser uma data futura")
    private LocalDateTime dataAgendada;

    private Integer volumeEstimadoMl;

    public UUID getDoadoraId() { return doadoraId; }
    public void setDoadoraId(UUID doadoraId) { this.doadoraId = doadoraId; }

    public UUID getCorredorId() { return corredorId; }
    public void setCorredorId(UUID corredorId) { this.corredorId = corredorId; }

    public LocalDateTime getDataAgendada() { return dataAgendada; }
    public void setDataAgendada(LocalDateTime dataAgendada) { this.dataAgendada = dataAgendada; }

    public Integer getVolumeEstimadoMl() { return volumeEstimadoMl; }
    public void setVolumeEstimadoMl(Integer volumeEstimadoMl) { this.volumeEstimadoMl = volumeEstimadoMl; }
}
