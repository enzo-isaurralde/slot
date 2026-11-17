package com.casino.slot.wallet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class WalletServiceTest {

    @Autowired
    private WalletService walletService;

    @Test
    void debeCrearBilleteraYRegistrarMovimientos() {
        // 1. Creamos una billetera para el usuario 1
        Wallet wallet = walletService.crearBilletera(1L);
        assertEquals(10000L, wallet.getBalance());

        // 2. Registramos una apuesta de 100 (resta)
        walletService.registrarMovimiento(wallet.getId(), "APUESTA", -100L, "spin-001");
        assertEquals(9900L, walletService.obtenerSaldo(1L));

        // 3. Registramos un premio de 500 (suma)
        walletService.registrarMovimiento(wallet.getId(), "PREMIO", 500L, "spin-001");
        assertEquals(10400L, walletService.obtenerSaldo(1L));

        // 4. Verificamos que el historial tenga 2 movimientos
        var historial = walletService.obtenerHistorial(wallet.getId());
        assertEquals(2, historial.size());
    }
}