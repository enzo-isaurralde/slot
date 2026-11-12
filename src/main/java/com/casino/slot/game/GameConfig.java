package com.casino.slot.game;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class GameConfig {
    private Game game;
    private List<GameSymbol> symbols;
    private Map<Integer, List<String>> reelStrips; // Rodillo -> Lista de Símbolos
    private List<Payline> paylines;
    private List<Paytable> paytable;
    private List<BonusConfig> bonusConfigs;
}