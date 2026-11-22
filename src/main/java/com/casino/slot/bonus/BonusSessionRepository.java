package com.casino.slot.bonus;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BonusSessionRepository extends JpaRepository<BonusSession, Long> {
    Optional<BonusSession> findByUserIdAndEstado(Long userId, String estado);
}