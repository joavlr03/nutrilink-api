package br.com.joavlr03.nutrilink_api.dto.corredorlogistico;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** Atualização do corredor. A homologação é controlada por /habilitar e /desabilitar. */
public class CorredorLogisticoUpdateRequest {

    @NotBlank(message = "Nome do corredor é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nomeCorredor;

    @NotBlank(message = "CEPs atendidos são obrigatórios")
    @Pattern(regexp = "\\d{3}(\\s*,\\s*\\d{3})*",
             message = "Informe prefixos de CEP com 3 dígitos separados por vírgula (ex.: 070,071,072)")
    private String cepsAtendidos;

    public String getNomeCorredor() { return nomeCorredor; }
    public void setNomeCorredor(String nomeCorredor) { this.nomeCorredor = nomeCorredor; }

    public String getCepsAtendidos() { return cepsAtendidos; }
    public void setCepsAtendidos(String cepsAtendidos) { this.cepsAtendidos = cepsAtendidos; }
}
