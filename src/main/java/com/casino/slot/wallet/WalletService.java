package com.casino.slot.wallet;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletTransactionRepository transactionRepository;

    @Transactional
    public Wallet crearBilletera(Long userId) {
        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet.setBalance(10000L); // 100.00 pesos de regalo
        wallet.setVersion(0);
        return walletRepository.save(wallet);
    }

    @Transactional
    public void registrarMovimiento(Long walletId, String type, Long amount, String reference) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Billetera no encontrada"));

        // Actualizamos el saldo
        wallet.setBalance(wallet.getBalance() + amount);
        walletRepository.save(wallet);

        // Registramos el movimiento (inmutable)
        WalletTransaction tx = new WalletTransaction();
        tx.setWalletId(walletId);
        tx.setType(type);
        tx.setAmount(amount);
        tx.setBalanceAfter(wallet.getBalance());
        tx.setReference(reference);
        transactionRepository.save(tx);
    }

    public Long obtenerSaldo(Long userId) {
        return walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Billetera no encontrada"))
                .getBalance();
    }

    public List<WalletTransaction> obtenerHistorial(Long walletId) {
        return transactionRepository.findByWalletIdOrderByCreatedAtDesc(walletId);
    }
}