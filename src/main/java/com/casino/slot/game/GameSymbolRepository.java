package com.casino.slot.game;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface GameSymbolRepository extends JpaRepository<GameSymbol, Long> {
    List<GameSymbol> findByGameId(Long gameId);
}