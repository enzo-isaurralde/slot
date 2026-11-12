package com.casino.slot.engine;

import com.casino.slot.game.Payline;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaylineEvaluator {

    /**
     * Evalúa una línea de pago en la grilla y devuelve el símbolo ganador.
     *
     * @param grilla  La grilla 5x3 (5 rodillos, 3 filas cada uno).
     * @param payline La línea de pago (r1 a r5 indican qué fila tomar de cada rodillo).
     * @return El símbolo que ganó (o null si no hay al menos 3 iguales).
     */
    public String evaluarLinea(List<List<String>> grilla, Payline payline) {
        // 1. Extraemos los 5 símbolos de la línea según las filas de la payline
        // grilla.get(rodillo).get(fila)
        String s1 = grilla.get(0).get(payline.getR1());
        String s2 = grilla.get(1).get(payline.getR2());
        String s3 = grilla.get(2).get(payline.getR3());
        String s4 = grilla.get(3).get(payline.getR4());
        String s5 = grilla.get(4).get(payline.getR5());

        String[] simbolos = {s1, s2, s3, s4, s5};

        // 2. Buscamos el símbolo base (el primero que no sea Wild o Scatter)
        String simboloBase = null;
        for (String s : simbolos) {
            if (!s.equals("WILD") && !s.equals("SCATTER")) {
                simboloBase = s;
                break;
            }
        }

        // Si todos son Wilds, ganamos con Wild (aunque es rarísimo)
        if (simboloBase == null) simboloBase = "WILD";

        // Si arranca con Scatter, no hay línea ganadora (el Scatter paga aparte)
        if (simboloBase.equals("SCATTER")) return null;

        // 3. Contamos cuántos símbolos consecutivos hay (el Wild comodín cuenta)
        int consecutivos = 0;
        for (String s : simbolos) {
            if (s.equals(simboloBase) || s.equals("WILD")) {
                consecutivos++;
            } else {
                break; // Se corta la racha
            }
        }

        // 4. Si hay 3 o más, devolvemos el símbolo ganador. Si no, null.
        if (consecutivos >= 3) {
            return simboloBase;
        }

        return null;
    }
}