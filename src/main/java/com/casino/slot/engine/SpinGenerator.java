package com.casino.slot.engine;

import com.casino.slot.game.GameConfig;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * Genera la grilla de 5x3 a partir de las cintas configuradas.
 * Utiliza SecureRandom para garantizar que el resultado sea criptográficamente seguro.
 */
@Component
public class SpinGenerator {

    private final SecureRandom random;

    public SpinGenerator() {
        this.random = new SecureRandom();
    }

    /**
     * Genera una grilla de 5 rodillos x 3 filas.
     *
     * @param config La configuración del juego que contiene las cintas.
     * @return Una lista de 5 rodillos, donde cada rodillo es una lista de 3 símbolos.
     */
    public List<List<String>> generarGrilla(GameConfig config) {
        List<List<String>> grilla = new ArrayList<>();

        // Iteramos por cada rodillo (0 a 4)
        for (int rodillo = 0; rodillo < 5; rodillo++) {
            List<String> cinta = config.getReelStrips().get(rodillo);

            if (cinta == null || cinta.isEmpty()) {
                throw new RuntimeException("El rodillo " + rodillo + " no tiene cinta configurada.");
            }

            // Elegimos una posición al azar en la cinta
            int posicionAleatoria = random.nextInt(cinta.size());

            // Tomamos 3 símbolos consecutivos (la cinta es circular)
            List<String> filaRodillo = new ArrayList<>();
            for (int fila = 0; fila < 3; fila++) {
                // El operador % nos asegura que si llegamos al final de la cinta,
                // volvamos a empezar desde el principio (comportamiento circular del slot).
                int indiceReal = (posicionAleatoria + fila) % cinta.size();
                filaRodillo.add(cinta.get(indiceReal));
            }

            grilla.add(filaRodillo);
        }

        return grilla;
    }
}