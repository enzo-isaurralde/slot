package com.casino.slot.spin;

import com.casino.slot.user.User;
import com.casino.slot.user.UserRepository;
import com.casino.slot.wallet.Wallet;
import com.casino.slot.wallet.WalletRepository;
import com.casino.slot.wallet.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class SpinServiceTest {

    @Autowired
    private SpinService spinService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private SpinRepository spinRepository;

    @Autowired
    private WalletService walletService;

    @BeforeEach
    void limpiarBaseDeDatos() {
        spinRepository.deleteAll();
        walletRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void debeRealizarUnGiroCompleto() {
        // 1. Creamos un usuario y su billetera
        User player = new User();
        player.setUsername("player_spin");
        player.setPassword("pass");
        player.setRole("PLAYER");
        User savedPlayer = userRepository.save(player);
        Wallet wallet = walletService.crearBilletera(savedPlayer.getId());

        // 2. Verificamos saldo inicial
        assertEquals(10000L, wallet.getBalance());

        // 3. Realizamos un giro de 100 centavos
        Spin spin = spinService.girar(savedPlayer.getId(), "SLOT_MILITAR", new BigDecimal(100));

        // 4. Verificamos que se guardó el giro
        assertNotNull(spin.getId());
        assertEquals(new BigDecimal(100), spin.getBetAmount());

        // 5. Verificamos que el saldo cambió (puede ser mayor o menor según el premio)
        Long saldoFinal = walletService.obtenerSaldo(savedPlayer.getId());
        assertNotEquals(10000L, saldoFinal);
    }
}