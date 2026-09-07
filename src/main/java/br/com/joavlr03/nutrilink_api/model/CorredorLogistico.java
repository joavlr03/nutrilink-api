package br.com.joavlr03.nutrilink_api.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "tb_corredores_logisticos")
public class CorredorLogistico {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String nomeCorredor;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String cepsAtendidos;

    @Column(nullable = false)
    private Boolean statusHomologacao;

    public CorredorLogistico() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNomeCorredor() { return nomeCorredor; }
    public void setNomeCorredor(String nomeCorredor) { this.nomeCorredor = nomeCorredor; }

    public String getCepsAtendidos() { return cepsAtendidos; }
    public void setCepsAtendidos(String cepsAtendidos) { this.cepsAtendidos = cepsAtendidos; }

    public Boolean getStatusHomologacao() { return statusHomologacao; }
    public void setStatusHomologacao(Boolean statusHomologacao) { this.statusHomologacao = statusHomologacao; }
}
