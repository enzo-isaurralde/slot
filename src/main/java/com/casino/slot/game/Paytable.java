package com.casino.slot.game;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "paytable")
public class Paytable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "game_id")
    private Long gameId;

    @Column(name = "symbol_id")
    private String symbolId;

    private Integer cantidad;
    private BigDecimal multiplicador;
}