package com.casino.slot.wallet;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RechargeRequestRepository extends JpaRepository<RechargeRequest, Long> {
    List<RechargeRequest> findByUserIdAndStatus(Long userId, String status);
    List<RechargeRequest> findByStatus(String status);
}