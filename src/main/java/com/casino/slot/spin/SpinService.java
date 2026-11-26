package com.casino.slot.spin;

import com.casino.slot.engine.PayoutCalculator;
import com.casino.slot.engine.SpinGenerator;
import com.casino.slot.game.Game;
import com.casino.slot.game.GameConfig;
import com.casino.slot.game.GameConfigService;
import com.casino.slot.game.GameRepository;
import com.casino.slot.wallet.Wallet;
import com.casino.slot.wallet.WalletService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SpinService {

    private final SpinGenerator spinGenerator;
    private final PayoutCalculator payoutCalculator;
    private final GameConfigService gameConfigService;
    private final GameRepository gameRepository;
    private final WalletService walletService;
    private final SpinRepository spinRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Apuestas permitidas en CENTAVOS ($100, $200, $500, $1.000)
    private static final List<BigDecimal> APUESTAS_PERMITIDAS = List.of(
            new BigDecimal("10000"),
            new BigDecimal("20000"),
            new BigDecimal("50000"),
            new BigDecimal("100000")
    );

    @Transactional
    public Spin girar(Long userId, String gameCode, BigDecimal betAmount, String idempotencyKey) {
        // 0. Verificamos si ya existe un giro con este idempotencyKey
        Optional<Spin> existingSpin = spinRepository.findByIdempotencyKey(idempotencyKey);
        if (existingSpin.isPresent()) {
            return existingSpin.get();
        }

        // 1. Validamos que la apuesta sea uno de los niveles permitidos (en centavos)
        if (!APUESTAS_PERMITIDAS.contains(betAmount)) {
            throw new RuntimeException("La apuesta debe ser 100, 200, 500 o 1000 pesos (10000, 20000, 50000 o 100000 centavos)");
        }

        // 2. Verificamos que el juego existe y está activo
        Game game = gameRepository.findByCodigo(gameCode)
                .orElseThrow(() -> new RuntimeException("Juego no encontrado"));

        if (!game.getActivo()) {
            throw new RuntimeException("El juego está desactivado");
        }

        // 3. Verificamos el saldo del jugador
        Wallet wallet = walletService.obtenerBilleteraPorUserId(userId);
        if (wallet.getBalance() < betAmount.longValue()) {
            throw new RuntimeException("Saldo insuficiente. Tu saldo actual es: " + wallet.getBalance());
        }

        // 4. Descontamos la apuesta
        walletService.registrarMovimiento(wallet.getId(), "APUESTA", -betAmount.longValue(), "spin-pending");

        // 5. Generamos la grilla
        GameConfig config = gameConfigService.cargarConfiguracion(gameCode);
        List<List<String>> grilla = spinGenerator.generarGrilla(config);

        // 6. Calculamos el premio
        BigDecimal premio = payoutCalculator.calcularPremio(config, grilla, betAmount);
        BigDecimal premioScatter = payoutCalculator.calcularPremioScatter(config, grilla, betAmount);
        BigDecimal premioTotal = premio.add(premioScatter);

        // 7. Acreditamos el premio
        if (premioTotal.compareTo(BigDecimal.ZERO) > 0) {
            walletService.registrarMovimiento(wallet.getId(), "PREMIO", premioTotal.longValue(), "spin-win");
        }

        // 8. Guardamos el giro
        Spin spin = new Spin();
        spin.setUserId(userId);
        spin.setGameId(game.getId());
        spin.setBetAmount(betAmount);
        spin.setWinAmount(premioTotal);
        spin.setIsBonus(false);
        spin.setBalanceAfter(walletService.obtenerSaldo(userId));
        spin.setIdempotencyKey(idempotencyKey);

        try {
            spin.setGridJson(objectMapper.writeValueAsString(grilla));
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la grilla", e);
        }

        return spinRepository.save(spin);
    }
}