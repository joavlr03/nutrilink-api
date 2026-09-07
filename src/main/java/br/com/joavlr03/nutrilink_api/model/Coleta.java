package br.com.joavlr03.nutrilink_api.model;

import java.time.LocalDateTime;
import java.util.UUID;



import br.com.joavlr03.nutrilink_api.model.enums.StatusColeta;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@Table(name = "tb_coletas")
public class Coleta {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doadora_id", nullable = false)
    private Doadora doadora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "corredor_id", nullable = false)
    private CorredorLogistico corredor;

    @Column(nullable = false)
    private LocalDateTime dataAgendada;

    @Column(nullable = true)
    private Integer volumeEstimadoMl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusColeta statusColeta;

    public Coleta() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Doadora getDoadora() { return doadora; }
    public void setDoadora(Doadora doadora) { this.doadora = doadora; }

    public CorredorLogistico getCorredor() { return corredor; }
    public void setCorredor(CorredorLogistico corredor) { this.corredor = corredor; }

    public LocalDateTime getDataAgendada() { return dataAgendada; }
    public void setDataAgendada(LocalDateTime dataAgendada) { this.dataAgendada = dataAgendada; }

    public Integer getVolumeEstimadoMl() { return volumeEstimadoMl; }
    public void setVolumeEstimadoMl(Integer volumeEstimadoMl) { this.volumeEstimadoMl = volumeEstimadoMl; }

    public StatusColeta getStatusColeta() { return statusColeta; }
    public void setStatusColeta(StatusColeta statusColeta) { this.statusColeta = statusColeta; }
}
