package com.casino.slot;

import com.casino.slot.game.GameConfig;
import com.casino.slot.game.GameConfigService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class GameConfigServiceTest {

    @Autowired
    private GameConfigService gameConfigService;

    @Test
    void debeCargarLaConfiguracionCompletaDelTemaMilitar() {
        GameConfig config = gameConfigService.cargarConfiguracion("SLOT_MILITAR");

        assertNotNull(config, "La config no debe ser nula");
        assertEquals("Slot Rat - Tema Militar", config.getGame().getNombre());
        System.out.println(">>> ID DEL JUEGO EN EL TEST: " + config.getGame().getId());
        System.out.println(">>> CANTIDAD DE PAYLINES: " + config.getPaylines().size());
        assertEquals(10, config.getSymbols().size(), "Deben ser 10 símbolos");
        assertEquals(10, config.getPaylines().size(), "Deben ser 10 paylines");
        assertEquals(5, config.getReelStrips().size(), "Deben ser 5 rodillos");
        assertFalse(config.getBonusConfigs().isEmpty(), "Debe haber config de bonus");
    }
}