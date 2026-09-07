package br.com.joavlr03.nutrilink_api.dto.triagem;

import java.util.UUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TriagemCreateRequest {
    @NotNull (message = "ID da doadora é obrigatório")
    private UUID doadoraId;

    // profissionalId é opcional — preenchido apenas na revisão manual
    private UUID profissionalId;

    @NotBlank(message = "Respostas do questionário são obrigatórias")
    private String respostasQuestionario;

    // parecerProfissional é opcional — preenchido apenas na revisão manual
    private String parecerProfissional;

    public UUID getDoadoraId() { return doadoraId; }
    public void setDoadoraId(UUID doadoraId) { this.doadoraId = doadoraId; }

    public UUID getProfissionalId() { return profissionalId; }
    public void setProfissionalId(UUID profissionalId) { this.profissionalId = profissionalId; }

    public String getRespostasQuestionario() { return respostasQuestionario; }
    public void setRespostasQuestionario(String respostasQuestionario) { this.respostasQuestionario = respostasQuestionario; }

    public String getParecerProfissional() { return parecerProfissional; }
    public void setParecerProfissional(String parecerProfissional) { this.parecerProfissional = parecerProfissional; }
}
