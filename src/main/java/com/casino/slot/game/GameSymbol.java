package com.casino.slot.game;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "game_symbols")
public class GameSymbol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "game_id")
    private Long gameId;

    @Column(name = "symbol_id")
    private String symbolId;

    private String rol; // NORMAL, WILD, SCATTER
}