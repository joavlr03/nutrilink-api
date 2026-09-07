package br.com.joavlr03.nutrilink_api.dto.corredorlogistico;

import java.util.UUID;

public class CorredorLogisticoResponse {
    private UUID id;
    private String nomeCorredor;
    private String cepsAtendidos;
    private Boolean statusHomologacao;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNomeCorredor() { return nomeCorredor; }
    public void setNomeCorredor(String nomeCorredor) { this.nomeCorredor = nomeCorredor; }

    public String getCepsAtendidos() { return cepsAtendidos; }
    public void setCepsAtendidos(String cepsAtendidos) { this.cepsAtendidos = cepsAtendidos; }

    public Boolean getStatusHomologacao() { return statusHomologacao; }
    public void setStatusHomologacao(Boolean statusHomologacao) { this.statusHomologacao = statusHomologacao; }
}
