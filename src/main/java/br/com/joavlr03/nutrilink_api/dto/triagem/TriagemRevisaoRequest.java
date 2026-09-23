package br.com.joavlr03.nutrilink_api.dto.triagem;

import java.util.UUID;

import br.com.joavlr03.nutrilink_api.model.enums.StatusTriagem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Revisão humana de uma triagem PENDENTE_REVISAO feita por um ANALISTA_NIVEL_1.
 * decisao aceita apenas APROVADA ou REPROVADA.
 */
public class TriagemRevisaoRequest {

    @NotNull(message = "ID do analista responsável é obrigatório")
    private UUID profissionalId;

    @NotNull(message = "Decisão é obrigatória (APROVADA ou REPROVADA)")
    private StatusTriagem decisao;

    @NotBlank(message = "Parecer do profissional é obrigatório")
    @Size(max = 2000, message = "Parecer deve ter no máximo 2000 caracteres")
    private String parecerProfissional;

    public UUID getProfissionalId() { return profissionalId; }
    public void setProfissionalId(UUID profissionalId) { this.profissionalId = profissionalId; }

    public StatusTriagem getDecisao() { return decisao; }
    public void setDecisao(StatusTriagem decisao) { this.decisao = decisao; }

    public String getParecerProfissional() { return parecerProfissional; }
    public void setParecerProfissional(String parecerProfissional) { this.parecerProfissional = parecerProfissional; }
}
