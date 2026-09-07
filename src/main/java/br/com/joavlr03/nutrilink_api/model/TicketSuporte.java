package br.com.joavlr03.nutrilink_api.model;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.joavlr03.nutrilink_api.model.enums.StatusTicket;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_tickets_suporte")
public class TicketSuporte {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doadora_id", nullable = false)
    private Doadora doadora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profissional_id", nullable = true)
    private ProfissionalSaude profissional;

    @Column(nullable = false, length = 100)
    private String assunto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusTicket statusTicket;

    @Column(nullable = false)
    private LocalDateTime dataAbertura;

    public TicketSuporte() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Doadora getDoadora() { return doadora; }
    public void setDoadora(Doadora doadora) { this.doadora = doadora; }

    public ProfissionalSaude getProfissional() { return profissional; }
    public void setProfissional(ProfissionalSaude profissional) { this.profissional = profissional; }

    public String getAssunto() { return assunto; }
    public void setAssunto(String assunto) { this.assunto = assunto; }

    public StatusTicket getStatusTicket() { return statusTicket; }
    public void setStatusTicket(StatusTicket statusTicket) { this.statusTicket = statusTicket; }

    public LocalDateTime getDataAbertura() { return dataAbertura; }
    public void setDataAbertura(LocalDateTime dataAbertura) { this.dataAbertura = dataAbertura; }
}
