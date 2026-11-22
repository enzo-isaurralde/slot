package com.casino.slot.bonus;

import com.casino.slot.game.GameConfig;
import com.casino.slot.game.GameConfigService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class FreeSpinsFeatureTest {

    @Autowired
    private FreeSpinsFeature freeSpinsFeature;

    @Autowired
    private GameConfigService configService;

    @Test
    void debeActivarBonusConTresScatters() {
        GameConfig config = configService.cargarConfiguracion("SLOT_MILITAR");

        // Grilla con 3 Scatters
        List<List<String>> grilla = Arrays.asList(
                Arrays.asList("SCATTER", "SYM_1", "SYM_2"),
                Arrays.asList("SYM_3", "SCATTER", "SYM_4"),
                Arrays.asList("SYM_5", "SYM_6", "SCATTER"),
                Arrays.asList("SYM_1", "SYM_2", "SYM_3"),
                Arrays.asList("SYM_4", "SYM_5", "SYM_6")
        );

        assertTrue(freeSpinsFeature.isActivado(config, grilla));
        assertEquals(10, freeSpinsFeature.getGirosOtorgados(config, grilla));
        assertEquals(new BigDecimal("2.00"), freeSpinsFeature.getMultiplicador(config));
    }

    @Test
    void noDebeActivarBonusConMenosDeTresScatters() {
        GameConfig config = configService.cargarConfiguracion("SLOT_MILITAR");

        // Grilla con 2 Scatters
        List<List<String>> grilla = Arrays.asList(
                Arrays.asList("SCATTER", "SYM_1", "SYM_2"),
                Arrays.asList("SYM_3", "SCATTER", "SYM_4"),
                Arrays.asList("SYM_5", "SYM_6", "SYM_7"),
                Arrays.asList("SYM_1", "SYM_2", "SYM_3"),
                Arrays.asList("SYM_4", "SYM_5", "SYM_6")
        );

        assertFalse(freeSpinsFeature.isActivado(config, grilla));
    }

    @Test
    void debeOtorgar15GirosConCuatroScatters() {
        GameConfig config = configService.cargarConfiguracion("SLOT_MILITAR");

        // Grilla con 4 Scatters
        List<List<String>> grilla = Arrays.asList(
                Arrays.asList("SCATTER", "SYM_1", "SYM_2"),
                Arrays.asList("SYM_3", "SCATTER", "SYM_4"),
                Arrays.asList("SYM_5", "SYM_6", "SCATTER"),
                Arrays.asList("SYM_1", "SCATTER", "SYM_3"), // Cuarto Scatter
                Arrays.asList("SYM_4", "SYM_5", "SYM_6")
        );

        assertTrue(freeSpinsFeature.isActivado(config, grilla));
        assertEquals(15, freeSpinsFeature.getGirosOtorgados(config, grilla));
    }

    @Test
    void debeOtorgar20GirosConCincoScatters() {
        GameConfig config = configService.cargarConfiguracion("SLOT_MILITAR");

        // Grilla con 5 Scatters
        List<List<String>> grilla = Arrays.asList(
                Arrays.asList("SCATTER", "SYM_1", "SYM_2"),
                Arrays.asList("SYM_3", "SCATTER", "SYM_4"),
                Arrays.asList("SYM_5", "SYM_6", "SCATTER"),
                Arrays.asList("SYM_1", "SCATTER", "SYM_3"),
                Arrays.asList("SYM_4", "SYM_5", "SCATTER") // Quinto Scatter
        );

        assertTrue(freeSpinsFeature.isActivado(config, grilla));
        assertEquals(20, freeSpinsFeature.getGirosOtorgados(config, grilla));
    }
}