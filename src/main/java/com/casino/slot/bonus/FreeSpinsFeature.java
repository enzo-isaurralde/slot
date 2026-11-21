package com.casino.slot.bonus;

import com.casino.slot.game.GameConfig;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Implementación del bonus de giros gratis.
 * Cuenta los Scatters en la grilla y decide si se activa, cuántos giros da y con qué multiplicador.
 */
@Component
public class FreeSpinsFeature implements BonusFeature {

    private static final int SCATTERS_MINIMOS = 3;

    @Override
    public boolean isActivado(GameConfig config, List<List<String>> grilla) {
        long scatters = contarScatters(grilla);
        return scatters >= SCATTERS_MINIMOS;
    }

    @Override
    public int getGirosOtorgados(GameConfig config, List<List<String>> grilla) {
        long scatters = contarScatters(grilla);

        // Buscamos la configuración del bonus para esa cantidad de Scatters
        return config.getBonusConfigs().stream()
                .filter(b -> b.getScattersRequeridos() <= scatters)
                .mapToInt(b -> b.getGiros())
                .max()
                .orElse(0);
    }

    @Override
    public BigDecimal getMultiplicador(GameConfig config) {
        // Tomamos el multiplicador del primer bonus configurado
        return config.getBonusConfigs().stream()
                .findFirst()
                .map(b -> b.getMultiplicador())
                .orElse(BigDecimal.ONE);
    }

    private long contarScatters(List<List<String>> grilla) {
        return grilla.stream()
                .flatMap(List::stream)
                .filter(s -> s.equals("SCATTER"))
                .count();
    }
}