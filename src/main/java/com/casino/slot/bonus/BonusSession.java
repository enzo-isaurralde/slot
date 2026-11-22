package com.casino.slot.bonus;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Representa una sesión de bonus (giros gratis) activa o finalizada para un jugador.
 * Permite que el jugador retome su bonus si cierra la aplicación.
 */
@Data
@Entity
@Table(name = "bonus_sessions")
public class BonusSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long gameId;

    @Column(nullable = false)
    private Integer girosRestantes;

    @Column(nullable = false)
    private BigDecimal multiplicador;

    @Column(nullable = false)
    private BigDecimal premioAcumulado;

    /**
     * Estado de la sesión. Valores posibles: "ACTIVO" o "FINALIZADO".
     */
    @Column(nullable = false)
    private String estado;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.estado == null) this.estado = "ACTIVO";
        if (this.premioAcumulado == null) this.premioAcumulado = BigDecimal.ZERO;
    }
}