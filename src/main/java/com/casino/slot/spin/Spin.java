package com.casino.slot.spin;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "spins")
public class Spin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long gameId;

    @Column(nullable = false)
    private BigDecimal betAmount;

    @Column(nullable = false)
    private BigDecimal winAmount;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String gridJson; // La grilla guardada como JSON

    @Column(nullable = false)
    private Boolean isBonus;

    private Long balanceAfter;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(unique = true, nullable = false)
    private String idempotencyKey;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.isBonus == null) this.isBonus = false;
    }
}