package com.casino.slot.engine;

import com.casino.slot.game.GameConfig;
import com.casino.slot.game.GameConfigService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;



@ActiveProfiles("test")
@SpringBootTest
class PayoutCalculatorTest {

    @Autowired
    private PayoutCalculator calculator;

    @Autowired
    private GameConfigService configService;

    @Test
    void debeCalcularPremioDeUnaLineaGanadora() {
        // 1. Traemos la config real (con la paytable)
        GameConfig config = configService.cargarConfiguracion("SLOT_MILITAR");

        // 2. Armamos una grilla donde la línea 1 (del medio) tiene 3 SYM_1
        // La línea 1 del PDF es (1,1,1,1,1)
        List<List<String>> grilla = Arrays.asList(
                Arrays.asList("X", "SYM_1", "X"), // Rodillo 1
                Arrays.asList("X", "SYM_1", "X"), // Rodillo 2
                Arrays.asList("X", "SYM_1", "X"), // Rodillo 3
                Arrays.asList("X", "SYM_2", "X"), // Rodillo 4 (corta)
                Arrays.asList("X", "SYM_3", "X")  // Rodillo 5
        );

        // 3. Apuesta de 100 (10 por línea)
        BigDecimal apuestaTotal = new BigDecimal(100);

        // 4. Calculamos el premio
        BigDecimal premio = calculator.calcularPremio(config, grilla, apuestaTotal);

        // 5. Verificamos: SYM_1 con 3 de cantidad paga x2 (según nuestro seed de BD)
        // 10 (apuesta por línea) * 2 = 20
        assertEquals(0, new BigDecimal(20).compareTo(premio), "El premio debería ser 20");
    }

    @Test
    void noDebePagarSiNoHayLineasGanadoras() {
        GameConfig config = configService.cargarConfiguracion("SLOT_MILITAR");

        // Grilla sin coincidencias
        List<List<String>> grilla = Arrays.asList(
                Arrays.asList("SYM_1", "SYM_2", "SYM_3"),
                Arrays.asList("SYM_4", "SYM_5", "SYM_6"),
                Arrays.asList("SYM_7", "SYM_8", "WILD"),
                Arrays.asList("SCATTER", "SYM_1", "SYM_2"),
                Arrays.asList("SYM_3", "SYM_4", "SYM_5")
        );

        BigDecimal premio = calculator.calcularPremio(config, grilla, new BigDecimal(100));
        assertTrue(premio.compareTo(BigDecimal.ZERO) == 0, "El premio debería ser 0");
    }

    @Test
    void debeCalcularPremioDeScatter() {
        GameConfig config = configService.cargarConfiguracion("SLOT_MILITAR");

        // Armamos una grilla con 3 Scatters en distintas posiciones
        List<List<String>> grilla = Arrays.asList(
                Arrays.asList("SCATTER", "SYM_1", "SYM_2"),
                Arrays.asList("SYM_3", "SCATTER", "SYM_4"),
                Arrays.asList("SYM_5", "SYM_6", "SCATTER"), // Tercer scatter
                Arrays.asList("SYM_1", "SYM_2", "SYM_3"),
                Arrays.asList("SYM_4", "SYM_5", "SYM_6")
        );

        BigDecimal apuestaTotal = new BigDecimal(100);

        // En nuestro seed, el Scatter no tiene pago en la paytable (solo activa el bonus).
        // Así que el premio monetario debería ser 0, pero el test verifica que la lógica funcione.
        BigDecimal premio = calculator.calcularPremioScatter(config, grilla, apuestaTotal);

        // Si en tu base de datos cargaste un pago para el SCATTER, cambialo acá.
        // Por ahora asumimos que solo activa bonus y no paga monedas.
        assertEquals(0, BigDecimal.ZERO.compareTo(premio), "El Scatter no debería pagar monedas directas (solo activar bonus)");
    }

    @Test
    void noDebePagarScatterSiHayMenosDeTres() {
        GameConfig config = configService.cargarConfiguracion("SLOT_MILITAR");

        List<List<String>> grilla = Arrays.asList(
                Arrays.asList("SCATTER", "SYM_1", "SYM_2"),
                Arrays.asList("SYM_3", "SYM_4", "SYM_5"),
                Arrays.asList("SYM_6", "SYM_7", "SYM_8"),
                Arrays.asList("SYM_1", "SYM_2", "SYM_3"),
                Arrays.asList("SYM_4", "SYM_5", "SYM_6")
        );

        BigDecimal premio = calculator.calcularPremioScatter(config, grilla, new BigDecimal(100));
        assertEquals(0, premio.compareTo(BigDecimal.ZERO), "Con menos de 3 Scatters no paga");
    }


}