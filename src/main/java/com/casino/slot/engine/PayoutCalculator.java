package com.casino.slot.engine;

import com.casino.slot.game.GameConfig;
import com.casino.slot.game.Payline;
import com.casino.slot.game.Paytable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class PayoutCalculator {

    /**
     * Calcula el premio total de una grilla basándose en la configuración del juego.
     *
     * @param config      La configuración del juego (contiene la paytable y las paylines).
     * @param grilla      La grilla 5x3 generada.
     * @param apuestaTotal La apuesta total del giro (ej: 100).
     * @return El premio total ganado (en centavos o unidad mínima).
     */
    public BigDecimal calcularPremio(GameConfig config, List<List<String>> grilla, BigDecimal apuestaTotal) {
        BigDecimal premioTotal = BigDecimal.ZERO;

        // La apuesta se reparte entre las 10 líneas
        BigDecimal apuestaPorLinea = apuestaTotal.divide(new BigDecimal(10), 2, java.math.RoundingMode.HALF_UP);

        // Iteramos por cada una de las 10 líneas
        for (Payline payline : config.getPaylines()) {

            // 1. Evaluamos qué símbolo ganó (o null si no ganó nada)
            String simboloGanador = evaluarLinea(grilla, payline);

            if (simboloGanador != null) {
                // 2. Contamos cuántos símbolos consecutivos hay (usando la misma lógica del Evaluator)
                int consecutivos = contarConsecutivos(grilla, payline, simboloGanador);

                // 3. Buscamos el multiplicador en la paytable
                BigDecimal multiplicador = buscarMultiplicador(config.getPaytable(), simboloGanador, consecutivos);

                if (multiplicador != null) {
                    // 4. Calculamos el premio de esta línea y lo sumamos
                    BigDecimal premioLinea = multiplicador.multiply(apuestaPorLinea);
                    premioTotal = premioTotal.add(premioLinea);
                }
            }
        }

        return premioTotal;
    }

    // --- Métodos auxiliares (similares al PaylineEvaluator) ---

    private String evaluarLinea(List<List<String>> grilla, Payline payline) {
        String[] simbolos = {
                grilla.get(0).get(payline.getR1()),
                grilla.get(1).get(payline.getR2()),
                grilla.get(2).get(payline.getR3()),
                grilla.get(3).get(payline.getR4()),
                grilla.get(4).get(payline.getR5())
        };

        String simboloBase = null;
        for (String s : simbolos) {
            if (!s.equals("WILD") && !s.equals("SCATTER")) {
                simboloBase = s;
                break;
            }
        }
        if (simboloBase == null) simboloBase = "WILD";
        if (simboloBase.equals("SCATTER")) return null;

        int consecutivos = contarConsecutivos(simbolos, simboloBase);
        return consecutivos >= 3 ? simboloBase : null;
    }

    private int contarConsecutivos(String[] simbolos, String simboloBase) {
        int count = 0;
        for (String s : simbolos) {
            if (s.equals(simboloBase) || s.equals("WILD")) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }

    private int contarConsecutivos(List<List<String>> grilla, Payline payline, String simboloBase) {
        String[] simbolos = {
                grilla.get(0).get(payline.getR1()),
                grilla.get(1).get(payline.getR2()),
                grilla.get(2).get(payline.getR3()),
                grilla.get(3).get(payline.getR4()),
                grilla.get(4).get(payline.getR5())
        };
        return contarConsecutivos(simbolos, simboloBase);
    }

    private BigDecimal buscarMultiplicador(List<Paytable> paytable, String simbolo, int cantidad) {
        return paytable.stream()
                .filter(p -> p.getSymbolId().equals(simbolo) && p.getCantidad() == cantidad)
                .map(Paytable::getMultiplicador)
                .findFirst()
                .orElse(null);
    }
}