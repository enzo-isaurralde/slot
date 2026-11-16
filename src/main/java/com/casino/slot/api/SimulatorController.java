package com.casino.slot.api;

import com.casino.slot.engine.PayoutCalculator;
import com.casino.slot.engine.SpinGenerator;
import com.casino.slot.game.GameConfig;
import com.casino.slot.game.GameConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RestController
@RequestMapping("/api/admin/simulate")
@RequiredArgsConstructor
public class SimulatorController {

    private final GameConfigService configService;
    private final SpinGenerator spinGenerator;
    private final PayoutCalculator payoutCalculator;

    @GetMapping
    public SimulationResult simular(@RequestParam(defaultValue = "1000000") int giros) {

        // 1. Cargamos la config una sola vez (mucho más rápido que ir a la BD en cada giro)
        GameConfig config = configService.cargarConfiguracion("SLOT_MILITAR");

        BigDecimal apuestaTotal = new BigDecimal(100); // Apuesta fija de 100 por giro
        BigDecimal totalApostado = BigDecimal.ZERO;
        BigDecimal totalGanado = BigDecimal.ZERO;
        BigDecimal premioMaximo = BigDecimal.ZERO;
        int girosGanadores = 0;
        int girosConBonus = 0;

        System.out.println("Iniciando simulación de " + giros + " giros...");

        // 2. El bucle millonario
        for (int i = 0; i < giros; i++) {
            // Generamos la grilla
            List<List<String>> grilla = spinGenerator.generarGrilla(config);

            // Calculamos el premio de las líneas y el Scatter
            BigDecimal premioLineas = payoutCalculator.calcularPremio(config, grilla, apuestaTotal);
            BigDecimal premioScatter = payoutCalculator.calcularPremioScatter(config, grilla, apuestaTotal);

            // Sumamos todo el premio de este giro
            BigDecimal premioTotalGiro = premioLineas.add(premioScatter);

            // Acumulamos estadísticas
            totalApostado = totalApostado.add(apuestaTotal);
            totalGanado = totalGanado.add(premioTotalGiro);

            if (premioTotalGiro.compareTo(BigDecimal.ZERO) > 0) {
                girosGanadores++;
            }
            // Contamos Scatters en la grilla para saber si hay bonus real
            int scattersEnGrilla = 0;
            for (List<String> rodillo : grilla) {
                for (String simbolo : rodillo) {
                    if (simbolo.equals("SCATTER")) {
                        scattersEnGrilla++;
                    }
                }
            }

            if (scattersEnGrilla >= 3) {
                girosConBonus++;
            }

            if (premioTotalGiro.compareTo(premioMaximo) > 0) {
                premioMaximo = premioTotalGiro;
            }

            // Mostrar progreso cada 100.000 giros
            if (i > 0 && i % 100000 == 0) {
                System.out.println("Procesados " + i + " giros...");
            }
        }

        // 3. Calculamos el RTP (Return To Player)
        BigDecimal rtp = BigDecimal.ZERO;
        if (totalApostado.compareTo(BigDecimal.ZERO) > 0) {
            // RTP = (Total Ganado / Total Apostado) * 100
            rtp = totalGanado.divide(totalApostado, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(100))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        // 4. Armamos y devolvemos la respuesta (Lombok se encarga de los setters)
        SimulationResult result = new SimulationResult();
        result.setTotalGiros(giros);
        result.setTotalApostado(totalApostado);
        result.setTotalGanado(totalGanado);
        result.setRtp(rtp);
        result.setGirosGanadores(girosGanadores);
        result.setGirosConBonus(girosConBonus);
        result.setPremioMaximo(premioMaximo);

        System.out.println("Simulación terminada. RTP: " + rtp + "%");

        return result;
    }
}