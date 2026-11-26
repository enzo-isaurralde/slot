package com.casino.slot.api;

import com.casino.slot.spin.Spin;
import com.casino.slot.spin.SpinRepository;
import com.casino.slot.spin.SpinService;
import com.casino.slot.user.RateLimiter;
import com.casino.slot.user.User;
import com.casino.slot.user.UserRepository;
import com.casino.slot.wallet.Wallet;
import com.casino.slot.wallet.WalletRepository;
import com.casino.slot.wallet.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/spins")
@RequiredArgsConstructor
public class SpinController {

    private final SpinService spinService;
    private final SpinRepository spinRepository;
    private final UserRepository userRepository;
    private final RateLimiter rateLimiter;
    private final WalletService walletService;
    private final WalletRepository walletRepository;

    /**
     * Realiza un giro para el usuario autenticado.
     * Aplica rate limiting basado en el nombre de usuario.
     *
     * @param request Datos del giro.
     * @param authentication Contexto de seguridad.
     * @return El giro procesado.
     */
    @PostMapping
    public Spin realizarGiro(@RequestBody SpinRequest request, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);

        if (!rateLimiter.tryConsume(user.getUsername())) {
            throw new RuntimeException("Demasiados giros en poco tiempo. Espere un momento.");
        }

        String idempotencyKey = request.getIdempotencyKey() != null
                ? request.getIdempotencyKey()
                : UUID.randomUUID().toString();

        return spinService.girar(user.getId(), request.getGameCode(), request.getBetAmount(), idempotencyKey);
    }




    /**
     * Obtiene el historial de giros del usuario autenticado, paginado y ordenado por fecha descendente.
     *
     * @param page Número de página (por defecto 0).
     * @param size Tamaño de la página (por defecto 10).
     * @param authentication Contexto de seguridad con el usuario autenticado.
     * @return Una página con los giros del usuario.
     */
    @GetMapping
    public Page<Spin> obtenerHistorial(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {

        User user = getAuthenticatedUser(authentication);
        Pageable pageable = PageRequest.of(page, size);
        return spinRepository.findByUserIdOrderByCreatedAtDesc(user.getId(), pageable);
    }

    private User getAuthenticatedUser(Authentication authentication) {
        String username = (authentication != null)
                ? authentication.getName()
                : "player_test";

        return userRepository.findByUsername(username)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUsername(username);
                    newUser.setPassword("password123");
                    newUser.setRole("PLAYER");
                    User savedUser = userRepository.save(newUser);

                    // Creamos la billetera con saldo inicial de $10.000 (1.000.000 centavos)
                    Wallet wallet = walletService.crearBilletera(savedUser.getId());
                    wallet.setBalance(1000000L); // <-- SALDO INICIAL DE $10.000
                    walletRepository.save(wallet);

                    return savedUser;
                });
    }

    @Data
    public static class SpinRequest {
        private String gameCode;
        private BigDecimal betAmount;
        private String idempotencyKey;
    }
}