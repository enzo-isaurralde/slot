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
class BonusFlowIntegrationTest {

    @Autowired
    private BonusService bonusService;

    @Autowired
    private GameConfigService configService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BonusSessionRepository bonusSessionRepository;

    private User savedUser;
    private GameConfig config;

    @BeforeEach
    void setUp() {
        bonusSessionRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("bonus_flow_player");
        user.setPassword("pass");
        user.setRole("PLAYER");
        savedUser = userRepository.save(user);

        config = configService.cargarConfiguracion("SLOT_MILITAR");
    }

    @Test
    void debeEjecutarFlujoCompletoDelBonus() {
        // 1. Simulamos una grilla con 3 Scatters (activa el bonus)
        List<List<String>> grillaInicial = Arrays.asList(
                Arrays.asList("SCATTER", "SYM_1", "SYM_2"),
                Arrays.asList("SYM_3", "SCATTER", "SYM_4"),
                Arrays.asList("SYM_5", "SYM_6", "SCATTER"),
                Arrays.asList("SYM_1", "SYM_2", "SYM_3"),
                Arrays.asList("SYM_4", "SYM_5", "SYM_6")
        );

        // 2. Creamos la sesión de bonus
        BonusSession session = bonusService.crearSesion(savedUser.getId(), 1L, config, grillaInicial);
        assertEquals(10, session.getGirosRestantes());
        assertEquals("ACTIVO", session.getEstado());

        // 3. Consumimos 3 giros con premio base de 100 cada uno
        for (int i = 0; i < 3; i++) {
            session = bonusService.consumirGiroConMultiplicador(session.getId(), new BigDecimal(100));
        }

        // 4. Verificamos que quedan 7 giros y el premio acumulado es 600 (3 * 100 * 2)
        assertEquals(7, session.getGirosRestantes());
        assertEquals(0, new BigDecimal(600).compareTo(session.getPremioAcumulado()));

        // 5. Simulamos un retrigger (salen 3 Scatters de nuevo)
        session = bonusService.agregarGiros(session.getId(), 10);
        assertEquals(17, session.getGirosRestantes());

        // 6. Consumimos los 17 giros restantes (todos con premio base 0 para simplificar)
        for (int i = 0; i < 17; i++) {
            session = bonusService.consumirGiroConMultiplicador(session.getId(), BigDecimal.ZERO);
        }

        // 7. Verificamos que la sesión terminó
        assertEquals(0, session.getGirosRestantes());
        assertEquals("FINALIZADO", session.getEstado());
    }
}