package br.com.joavlr03.nutrilink_api.model;

import br.com.joavlr03.nutrilink_api.model.enums.StatusTriagem;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_triagens")
public class Triagem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doadora_id", nullable = false)
    private Doadora doadora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profissional_id", nullable = true)
    private ProfissionalSaude profissional;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String respostasQuestionario;

    @Column(nullable = false)
    private Integer scoreRisco;

    @Column(nullable = false)
    private Boolean requerValidacaoHumana;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusTriagem statusTriagem;

    @Column(columnDefinition = "TEXT")
    private String parecerProfissional;

    @Column(nullable = false)
    private LocalDateTime dataRealizacao;

    public Triagem() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Doadora getDoadora() { return doadora; }
    public void setDoadora(Doadora doadora) { this.doadora = doadora; }

    public ProfissionalSaude getProfissional() { return profissional; }
    public void setProfissional(ProfissionalSaude profissional) { this.profissional = profissional; }

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