package br.com.joavlr03.nutrilink_api.dto.triagem;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.joavlr03.nutrilink_api.model.enums.StatusTriagem;

public class TriagemResponse {
      private UUID id;
    private UUID doadoraId;
    private UUID profissionalId;
    private String respostasQuestionario;
    private Integer scoreRisco;
    private Boolean requerValidacaoHumana;
    private StatusTriagem statusTriagem;
    private String parecerProfissional;
    private LocalDateTime dataRealizacao;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getDoadoraId() { return doadoraId; }
    public void setDoadoraId(UUID doadoraId) { this.doadoraId = doadoraId; }

    public UUID getProfissionalId() { return profissionalId; }
    public void setProfissionalId(UUID profissionalId) { this.profissionalId = profissionalId; }

    public String getRespostasQuestionario() { return respostasQuestionario; }
    public void setRespostasQuestionario(String respostasQuestionario) { this.respostasQuestionario = respostasQuestionario; }

    public Integer getScoreRisco() { return scoreRisco; }
    public void setScoreRisco(Integer scoreRisco) { this.scoreRisco = scoreRisco; }

    public Boolean getRequerValidacaoHumana() { return requerValidacaoHumana; }
    public void setRequerValidacaoHumana(Boolean requerValidacaoHumana) { this.requerValidacaoHumana = requerValidacaoHumana; }

    public StatusTriagem getStatusTriagem() { return statusTriagem; }
    public void setStatusTriagem(StatusTriagem statusTriagem) { this.statusTriagem = statusTriagem; }

    public String getParecerProfissional() { return parecerProfissional; }
    public void setParecerProfissional(String parecerProfissional) { this.parecerProfissional = parecerProfissional; }

    public LocalDateTime getDataRealizacao() { return dataRealizacao; }
    public void setDataRealizacao(LocalDateTime dataRealizacao) { this.dataRealizacao = dataRealizacao; }
}
