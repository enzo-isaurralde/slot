package com.casino.slot.api;

import com.casino.slot.spin.SpinRepository;
import com.casino.slot.user.User;
import com.casino.slot.user.UserRepository;
import com.casino.slot.wallet.WalletRepository;
import com.casino.slot.wallet.WalletService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class SpinControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private SpinRepository spinRepository;

    @Autowired
    private WalletService walletService;

    @Autowired
    private ObjectMapper objectMapper;

    private User savedPlayer;

    @BeforeEach
    void setUp() {
        spinRepository.deleteAll();
        walletRepository.deleteAll();
        userRepository.deleteAll();

        User player = new User();
        player.setUsername("player_api");
        player.setPassword("pass");
        player.setRole("PLAYER");
        savedPlayer = userRepository.save(player);
        walletService.crearBilletera(savedPlayer.getId());
    }

    @Test
    @WithMockUser(username = "player_api", roles = {"PLAYER"})
    void debeRealizarGiroYListarHistorial() throws Exception {
        // 1. Hacemos un giro
        Map<String, Object> giroRequest = Map.of(
                "gameCode", "SLOT_MILITAR",
                "betAmount", new BigDecimal(100)
        );

        mockMvc.perform(post("/api/spins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(giroRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.betAmount").value(100));

        // 2. Verificamos el historial
        mockMvc.perform(get("/api/spins")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].betAmount").value(100));
    }
}