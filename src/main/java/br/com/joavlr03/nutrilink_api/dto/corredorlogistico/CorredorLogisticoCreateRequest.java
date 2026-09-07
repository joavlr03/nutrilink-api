package br.com.joavlr03.nutrilink_api.dto.corredorlogistico;

import jakarta.validation.constraints.NotBlank;

public class CorredorLogisticoCreateRequest {
     @NotBlank(message = "Nome do corredor é obrigatório")
    private String nomeCorredor;

    @NotBlank(message = "CEPs atendidos são obrigatórios")
    private String cepsAtendidos;

    public String getNomeCorredor() { return nomeCorredor; }
    public void setNomeCorredor(String nomeCorredor) { this.nomeCorredor = nomeCorredor; }

    public String getCepsAtendidos() { return cepsAtendidos; }
    public void setCepsAtendidos(String cepsAtendidos) { this.cepsAtendidos = cepsAtendidos; }
}
