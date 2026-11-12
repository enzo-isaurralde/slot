package com.casino.slot.game;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaytableRepository extends JpaRepository<Paytable, Long> {
    List<Paytable> findByGameId(Long gameId); // <--- Corregido
}
