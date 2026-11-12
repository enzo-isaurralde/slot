package com.casino.slot.game;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "reel_strips")
public class ReelStrip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "game_id")
    private Long gameId;

    private Integer rodillo;
    private Integer posicion;

    @Column(name = "symbol_id")
    private String symbolId;
}