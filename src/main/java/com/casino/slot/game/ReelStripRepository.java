package com.casino.slot.game;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ReelStripRepository extends JpaRepository<ReelStrip, Long> {
    List<ReelStrip> findByGameIdOrderByRodilloAscPosicionAsc(Long gameId);
}