package com.casino.slot.api;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SimulationResult {
    private int totalGiros;
    private BigDecimal totalApostado;
    private BigDecimal totalGanado;
    private BigDecimal rtp;
    private int girosGanadores;
    private int girosConBonus;
    private BigDecimal premioMaximo;
}