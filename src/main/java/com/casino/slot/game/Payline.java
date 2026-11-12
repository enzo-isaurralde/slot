package com.casino.slot.game;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "paylines")
public class Payline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "game_id")
    private Long gameId;

    private Integer numero;
    private Integer r1;
    private Integer r2;
    private Integer r3;
    private Integer r4;
    private Integer r5;
}