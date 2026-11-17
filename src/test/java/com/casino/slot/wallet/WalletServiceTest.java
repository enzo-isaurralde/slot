package com.casino.slot.wallet;

import com.casino.slot.user.User;
import com.casino.slot.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @BeforeEach
    void limpiarBaseDeDatos() {
        // Limpiamos las tablas para que cada test empiece de cero
        walletRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void debeCrearBilleteraYRegistrarMovimientos() {
        // 1. Creamos un usuario de prueba
        User user = new User();
        user.setUsername("test_wallet_user");
        user.setPassword("password123");
        user.setRole("PLAYER");
        User savedUser = userRepository.save(user);

        // 2. Ahora sí, creamos la billetera para ese usuario
        Wallet wallet = walletService.crearBilletera(savedUser.getId());
        assertEquals(10000L, wallet.getBalance());

        // 3. Registramos una apuesta de 100 (resta)
        walletService.registrarMovimiento(wallet.getId(), "APUESTA", -100L, "spin-001");
        assertEquals(9900L, walletService.obtenerSaldo(savedUser.getId()));

        // 4. Registramos un premio de 500 (suma)
        walletService.registrarMovimiento(wallet.getId(), "PREMIO", 500L, "spin-001");
        assertEquals(10400L, walletService.obtenerSaldo(savedUser.getId()));

        // 5. Verificamos que el historial tenga 2 movimientos
        var historial = walletService.obtenerHistorial(wallet.getId());
        assertEquals(2, historial.size());
    }
}