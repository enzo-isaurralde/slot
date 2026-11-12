package com.casino.slot.game;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaylineRepository extends JpaRepository<Payline, Long> {
    List<Payline> findByGameId(Long gameId);
}