package com.casino.slot.wallet;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "wallet_transactions")
public class WalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long walletId;

    @Column(nullable = false)
    private String type; // APUESTA, PREMIO, RECARGA, AJUSTE

    @Column(nullable = false)
    private Long amount; // Monto en centavos (positivo o negativo)

    @Column(nullable = false)
    private Long balanceAfter; // Saldo después del movimiento

    private String reference; // Ej: ID del giro, ID de la recarga

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}