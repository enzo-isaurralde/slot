package com.casino.slot.engine;

import com.casino.slot.game.Payline;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaylineEvaluatorTest {

    private PaylineEvaluator evaluator;
    private Payline lineaMedia; // La línea 1 del PDF (todas filas = 1)

    @BeforeEach
    void setUp() {
        evaluator = new PaylineEvaluator();

        // Simulamos la payline 1 (recta del medio)
        lineaMedia = new Payline();
        lineaMedia.setR1(1); lineaMedia.setR2(1); lineaMedia.setR3(1);
        lineaMedia.setR4(1); lineaMedia.setR5(1);
    }

    @Test
    void debeGanarConTresIguales() {
        // Grilla de prueba: fila del medio tiene SYM_1, SYM_1, SYM_1, SYM_2, SYM_3
        List<List<String>> grilla = Arrays.asList(
                Arrays.asList("SYM_2", "SYM_1", "SYM_2"), // Rodillo 1, fila 1 = SYM_1
                Arrays.asList("SYM_3", "SYM_1", "SYM_3"), // Rodillo 2, fila 1 = SYM_1
                Arrays.asList("SYM_4", "SYM_1", "SYM_4"), // Rodillo 3, fila 1 = SYM_1
                Arrays.asList("SYM_5", "SYM_2", "SYM_5"), // Rodillo 4, fila 1 = SYM_2 (corta)
                Arrays.asList("SYM_6", "SYM_3", "SYM_6")  // Rodillo 5
        );

        String ganador = evaluator.evaluarLinea(grilla, lineaMedia);
        assertEquals("SYM_1", ganador, "Debería ganar SYM_1 con 3 consecutivos");
    }

    @Test
    void debeGanarConWildCompletandoLaCadena() {
        // Grilla de prueba basada en el PDF: Tanque, Tanque, Wild, Tanque, Avión
        List<List<String>> grilla = Arrays.asList(
                Arrays.asList("X", "SYM_8", "X"), // Rodillo 1 = SYM_8
                Arrays.asList("X", "SYM_8", "X"), // Rodillo 2 = SYM_8
                Arrays.asList("X", "WILD", "X"),  // Rodillo 3 = WILD (comodín)
                Arrays.asList("X", "SYM_8", "X"), // Rodillo 4 = SYM_8
                Arrays.asList("X", "SYM_1", "X")  // Rodillo 5 = SYM_1 (corta)
        );

        String ganador = evaluator.evaluarLinea(grilla, lineaMedia);
        assertEquals("SYM_8", ganador, "Debería ganar SYM_8 con 4 consecutivos (Wild completa)");
    }

    @Test
    void noDebeGanarConMenosDeTres() {
        // Grilla de prueba: solo 2 iguales al principio
        List<List<String>> grilla = Arrays.asList(
                Arrays.asList("X", "SYM_4", "X"),
                Arrays.asList("X", "SYM_4", "X"),
                Arrays.asList("X", "SYM_1", "X"), // Corta acá
                Arrays.asList("X", "SYM_4", "X"),
                Arrays.asList("X", "SYM_4", "X")
        );

        String ganador = evaluator.evaluarLinea(grilla, lineaMedia);
        assertNull(ganador, "No debería ganar porque solo hay 2 consecutivos");
    }
}