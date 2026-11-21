package com.casino.slot.bonus;

import com.casino.slot.game.GameConfig;

/**
 * Interfaz que define el contrato para cualquier tipo de bonus.
 * El motor del juego no sabe qué bonus está usando, solo llama a estos métodos.
 */
public interface BonusFeature {

    /**
     * Determina si la grilla actual activa el bonus.
     * @param config La configuración del juego.
     * @param grilla La grilla generada.
     * @return true si se activa el bonus, false en caso contrario.
     */
    boolean isActivado(GameConfig config, java.util.List<java.util.List<String>> grilla);

    /**
     * Calcula la cantidad de giros gratis que otorga el bonus según los Scatters.
     * @param config La configuración del juego.
     * @param grilla La grilla generada.
     * @return La cantidad de giros gratis.
     */
    int getGirosOtorgados(GameConfig config, java.util.List<java.util.List<String>> grilla);

    /**
     * Obtiene el multiplicador de premios durante el bonus.
     * @param config La configuración del juego.
     * @return El multiplicador (ej: 2.0 para x2).
     */
    java.math.BigDecimal getMultiplicador(GameConfig config);
}