package com.casino.slot.game;



import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codigo;
    private String nombre;
    private Boolean activo;
}