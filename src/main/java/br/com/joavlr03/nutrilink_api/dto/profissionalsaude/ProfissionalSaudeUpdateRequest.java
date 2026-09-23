package br.com.joavlr03.nutrilink_api.dto.profissionalsaude;

import br.com.joavlr03.nutrilink_api.model.enums.TipoProfissional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Atualização do profissional. A credencial é controlada por /inativar e /reativar. */
public class ProfissionalSaudeUpdateRequest {

    @NotBlank(message = "Nome completo é obrigatório")
    @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
    private String nomeCompleto;

    @NotBlank(message = "Registro do conselho é obrigatório")
    @Size(max = 50, message = "Registro deve ter no máximo 50 caracteres")
    private String registroConselho;

    @NotNull(message = "Tipo do profissional é obrigatório")
    private TipoProfissional tipoProfissional;

    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }

    public String getRegistroConselho() { return registroConselho; }
    public void setRegistroConselho(String registroConselho) { this.registroConselho = registroConselho; }

    public TipoProfissional getTipoProfissional() { return tipoProfissional; }
    public void setTipoProfissional(TipoProfissional tipoProfissional) { this.tipoProfissional = tipoProfissional; }
}
