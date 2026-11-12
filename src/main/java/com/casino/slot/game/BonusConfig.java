package com.casino.slot.game;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "bonus_config")
public class BonusConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "game_id")
    private Long gameId;

    private String tipo;

    @Column(name = "scatters_requeridos")
    private Integer scattersRequeridos;

    private Integer giros;
    private BigDecimal multiplicador;
}