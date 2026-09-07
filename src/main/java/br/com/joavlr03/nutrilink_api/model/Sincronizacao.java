package br.com.joavlr03.nutrilink_api.model;

import java.time.LocalDateTime;
import java.util.UUID;



import br.com.joavlr03.nutrilink_api.model.enums.StatusSincronizacao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sincronizacoes",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_sincronizacao_coleta",
            columnNames = "coleta_id"
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sincronizacao {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "coleta_id",
        nullable = false,
        unique = true,
        foreignKey = @ForeignKey(name = "fk_sincronizacao_coleta")
    )
    private Coleta coleta;

    @Lob
    @Column(
        name = "payload_enviado",
        nullable = false,
        columnDefinition = "TEXT"
    )
    private String payloadEnviado;

    @Column(name = "protocolo_gerado")
    private String protocoloGerado;

    @Enumerated(EnumType.STRING)
    @Column(
        name = "status_sincronizacao",
        nullable = false,
        length = 20
    )
    @Builder.Default
    private StatusSincronizacao statusSincronizacao =
            StatusSincronizacao.PENDENTE;

    @Column(name = "data_sincronizacao")
    private LocalDateTime dataSincronizacao;
}
