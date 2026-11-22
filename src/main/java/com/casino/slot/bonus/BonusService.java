package com.casino.slot.bonus;

import com.casino.slot.game.GameConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BonusService {

    private final BonusSessionRepository bonusSessionRepository;
    private final FreeSpinsFeature freeSpinsFeature;

    /**
     * Crea una nueva sesión de bonus para el usuario.
     */
    @Transactional
    public BonusSession crearSesion(Long userId, Long gameId, GameConfig config, java.util.List<java.util.List<String>> grilla) {
        BonusSession session = new BonusSession();
        session.setUserId(userId);
        session.setGameId(gameId);
        session.setGirosRestantes(freeSpinsFeature.getGirosOtorgados(config, grilla));
        session.setMultiplicador(freeSpinsFeature.getMultiplicador(config));
        session.setPremioAcumulado(BigDecimal.ZERO);
        session.setEstado("ACTIVO");
        return bonusSessionRepository.save(session);
    }

    /**
     * Busca la sesión de bonus activa de un usuario.
     */
    public Optional<BonusSession> obtenerSesionActiva(Long userId) {
        return bonusSessionRepository.findByUserIdAndEstado(userId, "ACTIVO");
    }

    /**
     * Resta un giro a la sesión y suma el premio acumulado.
     */
    @Transactional
    public BonusSession consumirGiro(Long sessionId, BigDecimal premio) {
        BonusSession session = bonusSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Sesión de bonus no encontrada"));

        session.setGirosRestantes(session.getGirosRestantes() - 1);
        session.setPremioAcumulado(session.getPremioAcumulado().add(premio));

        if (session.getGirosRestantes() <= 0) {
            session.setEstado("FINALIZADO");
        }

        return bonusSessionRepository.save(session);
    }
}