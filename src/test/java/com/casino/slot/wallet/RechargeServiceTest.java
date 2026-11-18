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
class RechargeServiceTest {

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private RechargeRequestRepository rechargeRepository;

    @BeforeEach
    void limpiarBaseDeDatos() {
        rechargeRepository.deleteAll();
        walletRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void debeCrearPedidoYQueElAdminLoApruebe() {
        // 1. Creamos un usuario (jugador)
        User player = new User();
        player.setUsername("player_test");
        player.setPassword("pass");
        player.setRole("PLAYER");
        User savedPlayer = userRepository.save(player);
        walletService.crearBilletera(savedPlayer.getId());

        // 2. Creamos un usuario ADMIN
        User admin = new User();
        admin.setUsername("admin_test");
        admin.setPassword("adminpass");
        admin.setRole("ADMIN");
        User savedAdmin = userRepository.save(admin);

        // 3. El jugador pide una recarga de 5000 centavos (50 pesos)
        RechargeRequest request = rechargeService.crearPedido(savedPlayer.getId(), 5000L);
        assertEquals("PENDING", request.getStatus());

        // 4. Verificamos que el saldo inicial siga siendo 10000
        assertEquals(10000L, walletService.obtenerSaldo(savedPlayer.getId()));

        // 5. El admin REAL (el que acabamos de crear) aprueba el pedido
        rechargeService.aprobarPedido(request.getId(), savedAdmin.getId());

        // 6. Verificamos que el saldo ahora sea 15000 (10000 + 5000)
        assertEquals(15000L, walletService.obtenerSaldo(savedPlayer.getId()));

        // 7. Verificamos que el pedido quedó como APPROVED
        RechargeRequest aprobado = rechargeRepository.findById(request.getId()).orElseThrow();
        assertEquals("APPROVED", aprobado.getStatus());
        assertEquals(savedAdmin.getId(), aprobado.getApprovedBy());
    }
}