package com.casino.slot.spin;

import com.casino.slot.user.User;
import com.casino.slot.user.UserRepository;
import com.casino.slot.wallet.Wallet;
import com.casino.slot.wallet.WalletRepository;
import com.casino.slot.wallet.WalletService;
import com.casino.slot.wallet.WalletTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.UUID;

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

    @Autowired
    private WalletTransactionRepository transactionRepository;

    private User savedPlayer;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll(); // Primero las transacciones
        spinRepository.deleteAll();
        walletRepository.deleteAll();      // Después las billeteras
        userRepository.deleteAll();        // Y al final los usuarios

        User player = new User();
        player.setUsername("player_spin");
        player.setPassword("pass");
        player.setRole("PLAYER");
        savedPlayer = userRepository.save(player);
        walletService.crearBilletera(savedPlayer.getId());
    }

    @Test
    void debeRealizarUnGiroCompleto() {
        String key = UUID.randomUUID().toString();
        Spin spin = spinService.girar(savedPlayer.getId(), "SLOT_MILITAR", new BigDecimal(100), key);
        assertNotNull(spin.getId());
        assertEquals(new BigDecimal(100), spin.getBetAmount());
        assertNotEquals(10000L, walletService.obtenerSaldo(savedPlayer.getId()));
    }

    @Test
    void debeRechazarApuestaNoPermitida() {
        String key = UUID.randomUUID().toString();
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            spinService.girar(savedPlayer.getId(), "SLOT_MILITAR", new BigDecimal(150), key);
        });
        assertEquals("La apuesta debe ser 100, 200, 500 o 1000", exception.getMessage());
    }

    @Test
    void debeRechazarSaldoInsuficiente() {
        User poorPlayer = new User();
        poorPlayer.setUsername("poor_player");
        poorPlayer.setPassword("pass");
        poorPlayer.setRole("PLAYER");
        User savedPoorPlayer = userRepository.save(poorPlayer);

        Wallet wallet = walletService.crearBilletera(savedPoorPlayer.getId());
        wallet.setBalance(500L);
        walletRepository.save(wallet);

        String key = UUID.randomUUID().toString();
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            spinService.girar(savedPoorPlayer.getId(), "SLOT_MILITAR", new BigDecimal(1000), key);
        });

        assertTrue(exception.getMessage().contains("Saldo insuficiente"));
    }

    @Test
    void debeSerIdempotenteConElMismoIdDeGiro() {
        String idempotencyKey = UUID.randomUUID().toString();

        // Primer giro
        Spin spin1 = spinService.girar(savedPlayer.getId(), "SLOT_MILITAR", new BigDecimal(100), idempotencyKey);
        Long saldoDespuesDelPrimerGiro = walletService.obtenerSaldo(savedPlayer.getId());

        // Segundo giro con el mismo idempotencyKey
        Spin spin2 = spinService.girar(savedPlayer.getId(), "SLOT_MILITAR", new BigDecimal(100), idempotencyKey);

        // Verificamos que es el mismo giro y que el saldo no cambió
        assertEquals(spin1.getId(), spin2.getId());
        assertEquals(saldoDespuesDelPrimerGiro, walletService.obtenerSaldo(savedPlayer.getId()));
    }
}