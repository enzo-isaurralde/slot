package com.casino.slot.bonus;

import com.casino.slot.game.GameConfig;
import com.casino.slot.game.GameConfigService;
import com.casino.slot.user.User;
import com.casino.slot.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
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
class BonusServiceTest {

    @Autowired
    private BonusService bonusService;

    @Autowired
    private GameConfigService configService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BonusSessionRepository bonusSessionRepository;

    private User savedUser;

    @BeforeEach
    void setUp() {
        bonusSessionRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("bonus_player");
        user.setPassword("pass");
        user.setRole("PLAYER");
        savedUser = userRepository.save(user);
    }

    @Test
    void debeCrearYConsumirSesionDeBonus() {
        GameConfig config = configService.cargarConfiguracion("SLOT_MILITAR");

        // Grilla con 3 Scatters (activa el bonus)
        List<List<String>> grilla = Arrays.asList(
                Arrays.asList("SCATTER", "SYM_1", "SYM_2"),
                Arrays.asList("SYM_3", "SCATTER", "SYM_4"),
                Arrays.asList("SYM_5", "SYM_6", "SCATTER"),
                Arrays.asList("SYM_1", "SYM_2", "SYM_3"),
                Arrays.asList("SYM_4", "SYM_5", "SYM_6")
        );

        // 1. Creamos la sesión
        BonusSession session = bonusService.crearSesion(savedUser.getId(), 1L, config, grilla);
        assertEquals(10, session.getGirosRestantes());
        assertEquals(new BigDecimal("2.00"), session.getMultiplicador());

        // 2. Consumimos un giro
        BonusSession actualizada = bonusService.consumirGiro(session.getId(), new BigDecimal(500));
        assertEquals(9, actualizada.getGirosRestantes());
        assertEquals(0, new BigDecimal(500).compareTo(actualizada.getPremioAcumulado()));

        // 3. Verificamos que se puede retomar
        assertTrue(bonusService.obtenerSesionActiva(savedUser.getId()).isPresent());
    }

    @Test
    void debeAplicarMultiplicadorAlConsumirGiro() {
        GameConfig config = configService.cargarConfiguracion("SLOT_MILITAR");

        // Creamos la sesión con la grilla de 3 Scatters
        List<List<String>> grilla = Arrays.asList(
                Arrays.asList("SCATTER", "SYM_1", "SYM_2"),
                Arrays.asList("SYM_3", "SCATTER", "SYM_4"),
                Arrays.asList("SYM_5", "SYM_6", "SCATTER"),
                Arrays.asList("SYM_1", "SYM_2", "SYM_3"),
                Arrays.asList("SYM_4", "SYM_5", "SYM_6")
        );
        BonusSession session = bonusService.crearSesion(savedUser.getId(), 1L, config, grilla);

        // Consumimos un giro con premio base de 100
        BonusSession actualizada = bonusService.consumirGiroConMultiplicador(session.getId(), new BigDecimal(100));

        // El premio acumulado debería ser 200 (100 * 2.00)
        assertEquals(0, new BigDecimal(200).compareTo(actualizada.getPremioAcumulado()));
        assertEquals(9, actualizada.getGirosRestantes());
    }

    @Test
    void debeAgregarGirosPorRetrigger() {
        GameConfig config = configService.cargarConfiguracion("SLOT_MILITAR");

        List<List<String>> grilla = Arrays.asList(
                Arrays.asList("SCATTER", "SYM_1", "SYM_2"),
                Arrays.asList("SYM_3", "SCATTER", "SYM_4"),
                Arrays.asList("SYM_5", "SYM_6", "SCATTER"),
                Arrays.asList("SYM_1", "SYM_2", "SYM_3"),
                Arrays.asList("SYM_4", "SYM_5", "SYM_6")
        );
        BonusSession session = bonusService.crearSesion(savedUser.getId(), 1L, config, grilla);
        assertEquals(10, session.getGirosRestantes());

        // Simulamos un retrigger (salen 3 Scatters de nuevo)
        BonusSession actualizada = bonusService.agregarGiros(session.getId(), 10);

        // Debería tener 20 giros
        assertEquals(20, actualizada.getGirosRestantes());
    }


}