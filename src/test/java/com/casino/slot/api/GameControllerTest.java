package com.casino.slot.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void debeListarJuegosYDevolverConfig() throws Exception {
        // 1. Probar GET /api/games
        mockMvc.perform(get("/api/games"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigo").value("SLOT_MILITAR"));

        // 2. Probar GET /api/games/1/config
        // Asumimos que el juego insertado tiene ID 1
        mockMvc.perform(get("/api/games/1/config"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.symbols.length()").value(10))
                .andExpect(jsonPath("$.paylines.length()").value(10));
    }
}