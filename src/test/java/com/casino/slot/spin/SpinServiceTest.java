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

    private User savedPlayer;

    @BeforeEach
    void setUp() {
        // Limpiamos la base
        spinRepository.deleteAll();
        walletRepository.deleteAll();
        userRepository.deleteAll();

        // Creamos un jugador para los tests
        User player = new User();
        player.setUsername("player_spin");
        player.setPassword("pass");
        player.setRole("PLAYER");
        savedPlayer = userRepository.save(player);
        walletService.crearBilletera(savedPlayer.getId());
    }

    @Test
    void debeRealizarUnGiroCompleto() {
        Spin spin = spinService.girar(savedPlayer.getId(), "SLOT_MILITAR", new BigDecimal(100));
        assertNotNull(spin.getId());
        assertEquals(new BigDecimal(100), spin.getBetAmount());
        assertNotEquals(10000L, walletService.obtenerSaldo(savedPlayer.getId()));
    }

    @Test
    void debeRechazarApuestaNoPermitida() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            spinService.girar(savedPlayer.getId(), "SLOT_MILITAR", new BigDecimal(150));
        });
        assertEquals("La apuesta debe ser 100, 200, 500 o 1000", exception.getMessage());
    }

    @Test
    void debeRechazarSaldoInsuficiente() {
        // 1. Creamos un usuario con saldo bajo (solo 500 centavos)
        User poorPlayer = new User();
        poorPlayer.setUsername("poor_player");
        poorPlayer.setPassword("pass");
        poorPlayer.setRole("PLAYER");
        User savedPoorPlayer = userRepository.save(poorPlayer);

        // Creamos la billetera y le seteamos el saldo manualmente a 500
        Wallet wallet = walletService.crearBilletera(savedPoorPlayer.getId());
        wallet.setBalance(500L);
        walletRepository.save(wallet);

        // 2. Intentamos girar con una apuesta de 1000 (que es mayor al saldo de 500)
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            spinService.girar(savedPoorPlayer.getId(), "SLOT_MILITAR", new BigDecimal(1000));
        });

        // 3. Verificamos que el mensaje sea el correcto
        assertTrue(exception.getMessage().contains("Saldo insuficiente"),
                "El mensaje debería contener 'Saldo insuficiente'");
    }
}