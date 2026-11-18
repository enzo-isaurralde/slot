package com.casino.slot.wallet;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RechargeService {

    private final RechargeRequestRepository rechargeRepository;
    private final WalletService walletService;

    // El jugador pide una recarga
    @Transactional
    public RechargeRequest crearPedido(Long userId, Long amount) {
        RechargeRequest request = new RechargeRequest();
        request.setUserId(userId);
        request.setAmount(amount);
        request.setStatus("PENDING");
        return rechargeRepository.save(request);
    }

    // El admin aprueba la recarga
    @Transactional
    public RechargeRequest aprobarPedido(Long requestId, Long adminId) {
        RechargeRequest request = rechargeRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        if (!request.getStatus().equals("PENDING")) {
            throw new RuntimeException("El pedido ya fue procesado");
        }

        // 1. Cambiamos el estado del pedido
        request.setStatus("APPROVED");
        request.setApprovedBy(adminId);
        request.setApprovedAt(LocalDateTime.now());
        rechargeRepository.save(request);

        // 2. Acreditamos el saldo en la billetera
        Wallet wallet = walletService.obtenerBilleteraPorUserId(request.getUserId());
        walletService.registrarMovimiento(wallet.getId(), "RECARGA", request.getAmount(), "recharge-" + request.getId());

        return request;
    }

    public List<RechargeRequest> obtenerPendientes() {
        return rechargeRepository.findByStatus("PENDING");
    }
}