package br.com.joavlr03.nutrilink_api.model;

import java.util.UUID;



import br.com.joavlr03.nutrilink_api.model.enums.TipoProfissional;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity 
public class ProfissionalSaude {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String nomeCompleto;

    @Column(nullable = false, unique = true, length = 50)
    private String registroConselho;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoProfissional tipoProfissional;

    @Column(nullable = false)
    private Boolean credencialAtiva;

    public ProfissionalSaude() {}

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
