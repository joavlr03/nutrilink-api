package br.com.joavlr03.nutrilink_api.dto.profissionalsaude;



import br.com.joavlr03.nutrilink_api.model.enums.TipoProfissional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProfissionalSaudeCreateRequest {
     @NotBlank(message = "Nome completo é obrigatório")
    private String nomeCompleto;

    @NotBlank(message = "Registro do conselho é obrigatório")
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
