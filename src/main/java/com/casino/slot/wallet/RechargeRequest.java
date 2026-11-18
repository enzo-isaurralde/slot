package com.casino.slot.wallet;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "recharge_requests")
public class RechargeRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long amount; // Monto en centavos (ej: 5000 = 50.00 pesos)

    @Column(nullable = false)
    private String status; // PENDING, APPROVED, REJECTED

    private Long approvedBy; // ID del admin que aprobó

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime approvedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = "PENDING";
    }
}