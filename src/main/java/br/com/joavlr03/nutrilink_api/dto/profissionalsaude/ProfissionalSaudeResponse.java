package br.com.joavlr03.nutrilink_api.dto.profissionalsaude;

import java.util.UUID;

import br.com.joavlr03.nutrilink_api.model.enums.TipoProfissional;

public class ProfissionalSaudeResponse {
    private UUID id;
    private String nomeCompleto;
    private String registroConselho;
    private TipoProfissional tipoProfissional;
    private Boolean credencialAtiva;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }

    public String getRegistroConselho() { return registroConselho; }
    public void setRegistroConselho(String registroConselho) { this.registroConselho = registroConselho; }

    public TipoProfissional getTipoProfissional() { return tipoProfissional; }
    public void setTipoProfissional(TipoProfissional tipoProfissional) { this.tipoProfissional = tipoProfissional; }

    public Boolean getCredencialAtiva() { return credencialAtiva; }
    public void setCredencialAtiva(Boolean credencialAtiva) { this.credencialAtiva = credencialAtiva; }
}
