package com.casino.slot;

import com.casino.slot.engine.SpinGenerator;
import com.casino.slot.game.GameConfig;
import com.casino.slot.game.GameConfigService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class SpinGeneratorTest {

    @Autowired
    private SpinGenerator spinGenerator;

    @Autowired
    private GameConfigService gameConfigService;

    @Test
    void debeGenerarGrillaDe5x3() {
        // Cargamos la config real desde la base de datos
        GameConfig config = gameConfigService.cargarConfiguracion("SLOT_MILITAR");

        // Generamos una grilla
        List<List<String>> grilla = spinGenerator.generarGrilla(config);

        // Validaciones
        assertNotNull(grilla, "La grilla no debe ser nula");
        assertEquals(5, grilla.size(), "La grilla debe tener 5 rodillos");

        for (List<String> rodillo : grilla) {
            assertEquals(3, rodillo.size(), "Cada rodillo debe tener 3 filas");
            for (String simbolo : rodillo) {
                assertNotNull(simbolo, "El símbolo no debe ser nulo");
                assertFalse(simbolo.isEmpty(), "El símbolo no debe estar vacío");
            }
        }
    }
}