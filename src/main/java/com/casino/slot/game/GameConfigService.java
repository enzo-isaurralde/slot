package com.casino.slot.game;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servicio encargado de leer la configuración del juego desde la base de datos
 * y ensamblarla en un objeto en memoria (GameConfig) que el motor pueda usar.
 *
 * Esto cumple con el principio de que "el motor no sabe de qué trata el juego":
 * simplemente recibe este objeto con los datos y reglas ya listos.
 */
@Service
public class GameConfigService {

    private final GameRepository gameRepo;
    private final GameSymbolRepository symbolRepo;
    private final ReelStripRepository reelRepo;
    private final PaylineRepository paylineRepo;
    private final PaytableRepository paytableRepo;
    private final BonusConfigRepository bonusRepo;

    // Inyección de dependencias por constructor (la forma recomendada en Spring)
    public GameConfigService(GameRepository gameRepo, GameSymbolRepository symbolRepo,
                             ReelStripRepository reelRepo, PaylineRepository paylineRepo,
                             PaytableRepository paytableRepo, BonusConfigRepository bonusRepo) {
        this.gameRepo = gameRepo;
        this.symbolRepo = symbolRepo;
        this.reelRepo = reelRepo;
        this.paylineRepo = paylineRepo;
        this.paytableRepo = paytableRepo;
        this.bonusRepo = bonusRepo;
    }

    /**
     * Busca un juego por su código (ej: "SLOT_MILITAR") y arma toda su configuración.
     *
     * @param codigoJuego El código único del juego en la base de datos.
     * @return Un objeto GameConfig con todos los datos listos para ser usados por el motor.
     * @throws RuntimeException si el juego no existe en la base de datos.
     */
    public GameConfig cargarConfiguracion(String codigoJuego) {
        // 1. Buscamos el juego principal. Si no existe, lanzamos un error para que no siga.
        Game game = gameRepo.findByCodigo(codigoJuego)
                .orElseThrow(() -> new RuntimeException("Juego no encontrado: " + codigoJuego));

        // 2. Preparamos el objeto que va a viajar hacia el motor
        GameConfig config = new GameConfig();

        // 3. Seteamos los datos básicos y las listas directas
        config.setGame(game);
        config.setSymbols(symbolRepo.findByGameId(game.getId()));
        config.setPaylines(paylineRepo.findByGameId(game.getId()));
        config.setPaytable(paytableRepo.findByGameId(game.getId()));
        config.setBonusConfigs(bonusRepo.findByGameId(game.getId()));

        // 4. Armamos las cintas (reel strips).
        // La base de datos nos devuelve una lista plana de todos los símbolos de todos los rodillos.
        // Tenemos que agruparlos en un Map (Diccionario) donde la clave sea el número de rodillo
        // y el valor sea la lista ordenada de símbolos que van en ese rodillo.
        List<ReelStrip> allStrips = reelRepo.findByGameIdOrderByRodilloAscPosicionAsc(game.getId());

        Map<Integer, List<String>> stripsMap = allStrips.stream()
                .collect(Collectors.groupingBy(
                        ReelStrip::getRodillo, // Clave: el número de rodillo (0 a 4)
                        Collectors.mapping(ReelStrip::getSymbolId, Collectors.toList()) // Valor: lista de IDs de símbolos en orden
                ));

        config.setReelStrips(stripsMap);

        // 5. Devolvemos la configuración completa
        return config;
    }
}