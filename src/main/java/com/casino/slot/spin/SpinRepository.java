package com.casino.slot.spin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SpinRepository extends JpaRepository<Spin, Long> {
    Page<Spin> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}