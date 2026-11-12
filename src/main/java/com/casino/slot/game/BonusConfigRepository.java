package com.casino.slot.game;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BonusConfigRepository extends JpaRepository<BonusConfig, Long> {
    List<BonusConfig> findByGameId(Long gameId);
}