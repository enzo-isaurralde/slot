package com.casino.slot.api;

import com.casino.slot.game.GameConfig;
import com.casino.slot.game.GameConfigService;
import com.casino.slot.game.Game;
import com.casino.slot.game.GameRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameConfigService configService;
    private final GameRepository gameRepo;

    public GameController(GameConfigService configService, GameRepository gameRepo) {
        this.configService = configService;
        this.gameRepo = gameRepo;
    }

    // GET /api/games
    @GetMapping
    public List<Game> listarJuegos() {
        return gameRepo.findAll();
    }

    // GET /api/games/{id}/config
    @GetMapping("/{id}/config")
    public GameConfig obtenerConfiguracion(@PathVariable Long id) {
        // Por ahora buscamos el código por ID y pedimos la config por código
        // En el futuro podemos optimizar esto
        Game game = gameRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Juego no encontrado"));

        return configService.cargarConfiguracion(game.getCodigo());
    }
}