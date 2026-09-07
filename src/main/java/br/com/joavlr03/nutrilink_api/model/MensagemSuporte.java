package br.com.joavlr03.nutrilink_api.model;

import java.time.LocalDateTime;
import java.util.UUID;


import br.com.joavlr03.nutrilink_api.model.enums.RemetenteTipo;
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
@Table(name = "tb_mensagens_suporte")
public class MensagemSuporte {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private TicketSuporte ticket;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RemetenteTipo remetenteTipo;

    @Column(nullable = false)
    private UUID remetenteId;

    @Column(nullable = false, length = 1000)
    private String conteudoMensagem;

    @Column(nullable = false)
    private LocalDateTime dataEnvio;

    public MensagemSuporte() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public TicketSuporte getTicket() { return ticket; }
    public void setTicket(TicketSuporte ticket) { this.ticket = ticket; }

    public RemetenteTipo getRemetenteTipo() { return remetenteTipo; }
    public void setRemetenteTipo(RemetenteTipo remetenteTipo) { this.remetenteTipo = remetenteTipo; }

    public UUID getRemetenteId() { return remetenteId; }
    public void setRemetenteId(UUID remetenteId) { this.remetenteId = remetenteId; }

    public String getConteudoMensagem() { return conteudoMensagem; }
    public void setConteudoMensagem(String conteudoMensagem) { this.conteudoMensagem = conteudoMensagem; }

    public LocalDateTime getDataEnvio() { return dataEnvio; }
    public void setDataEnvio(LocalDateTime dataEnvio) { this.dataEnvio = dataEnvio; }
}
