package com.casino.slot.wallet;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long balance; // Saldo en centavos (ej: 10000 = 100.00 pesos)

    @Version // Control de concurrencia (evita doble gasto)
    private Integer version;
}