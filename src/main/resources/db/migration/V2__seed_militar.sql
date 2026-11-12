-- 1. Insertar el juego
INSERT INTO games (codigo, nombre, rtp_objetivo)
VALUES ('SLOT_MILITAR', 'Slot Rat - Tema Militar', 95.00);

-- Guardamos el ID real del juego en una variable temporal para no depender del número 1
DO $$
DECLARE
v_game_id BIGINT;
BEGIN
SELECT id INTO v_game_id FROM games WHERE codigo = 'SLOT_MILITAR';

-- 2. Insertar los 10 símbolos
INSERT INTO game_symbols (game_id, symbol_id, rol, asset_key) VALUES
                                                                  (v_game_id, 'SYM_1', 'NORMAL', 'botas'), (v_game_id, 'SYM_2', 'NORMAL', 'cantimplora'), (v_game_id, 'SYM_3', 'NORMAL', 'cuchillo'),
                                                                  (v_game_id, 'SYM_4', 'NORMAL', 'mapa'), (v_game_id, 'SYM_5', 'NORMAL', 'brujula'), (v_game_id, 'SYM_6', 'NORMAL', 'granada'),
                                                                  (v_game_id, 'SYM_7', 'NORMAL', 'casco'), (v_game_id, 'SYM_8', 'NORMAL', 'tanque'), (v_game_id, 'WILD', 'WILD', 'medalla'),
                                                                  (v_game_id, 'SCATTER', 'SCATTER', 'estrella');

-- 3. Las 10 Líneas de Pago
INSERT INTO paylines (game_id, numero, r1, r2, r3, r4, r5) VALUES
                                                               (v_game_id, 1, 1, 1, 1, 1, 1), (v_game_id, 2, 0, 0, 0, 0, 0), (v_game_id, 3, 2, 2, 2, 2, 2),
                                                               (v_game_id, 4, 0, 1, 2, 1, 0), (v_game_id, 5, 2, 1, 0, 1, 2), (v_game_id, 6, 0, 0, 1, 2, 2),
                                                               (v_game_id, 7, 2, 2, 1, 0, 0), (v_game_id, 8, 1, 0, 0, 0, 1), (v_game_id, 9, 1, 2, 2, 2, 1),
                                                               (v_game_id, 10, 0, 1, 1, 1, 0);

-- 4. Pagos iniciales
INSERT INTO paytable (game_id, symbol_id, cantidad, multiplicador) VALUES
                                                                       (v_game_id, 'SYM_1', 3, 2), (v_game_id, 'SYM_1', 4, 5), (v_game_id, 'SYM_1', 5, 10),
                                                                       (v_game_id, 'SYM_2', 3, 2), (v_game_id, 'SYM_2', 4, 5), (v_game_id, 'SYM_2', 5, 10),
                                                                       (v_game_id, 'SYM_3', 3, 3), (v_game_id, 'SYM_3', 4, 8), (v_game_id, 'SYM_3', 5, 15),
                                                                       (v_game_id, 'SYM_4', 3, 3), (v_game_id, 'SYM_4', 4, 8), (v_game_id, 'SYM_4', 5, 15),
                                                                       (v_game_id, 'SYM_5', 3, 4), (v_game_id, 'SYM_5', 4, 10), (v_game_id, 'SYM_5', 5, 20),
                                                                       (v_game_id, 'SYM_6', 3, 5), (v_game_id, 'SYM_6', 4, 20), (v_game_id, 'SYM_6', 5, 50),
                                                                       (v_game_id, 'SYM_7', 3, 10), (v_game_id, 'SYM_7', 4, 40), (v_game_id, 'SYM_7', 5, 100),
                                                                       (v_game_id, 'SYM_8', 3, 15), (v_game_id, 'SYM_8', 4, 60), (v_game_id, 'SYM_8', 5, 200);

-- 5. Configuración del Bonus
INSERT INTO bonus_config (game_id, tipo, scatters_requeridos, giros, multiplicador, permite_retrigger) VALUES
                                                                                                           (v_game_id, 'FREE_SPINS', 3, 10, 2.00, TRUE),
                                                                                                           (v_game_id, 'FREE_SPINS', 4, 15, 2.00, TRUE),
                                                                                                           (v_game_id, 'FREE_SPINS', 5, 20, 2.00, TRUE);

-- 6. Cintas de los rodillos (Reel Strips)
-- Rodillo 0
INSERT INTO reel_strips (game_id, rodillo, posicion, symbol_id) VALUES
                                                                    (v_game_id, 0, 0, 'SYM_1'), (v_game_id, 0, 1, 'SYM_2'), (v_game_id, 0, 2, 'SYM_3'), (v_game_id, 0, 3, 'SYM_4'), (v_game_id, 0, 4, 'SYM_5'),
                                                                    (v_game_id, 0, 5, 'SYM_6'), (v_game_id, 0, 6, 'SYM_7'), (v_game_id, 0, 7, 'SYM_8'), (v_game_id, 0, 8, 'WILD'), (v_game_id, 0, 9, 'SCATTER');

-- Rodillo 1
INSERT INTO reel_strips (game_id, rodillo, posicion, symbol_id) VALUES
                                                                    (v_game_id, 1, 0, 'SYM_2'), (v_game_id, 1, 1, 'SYM_3'), (v_game_id, 1, 2, 'SYM_4'), (v_game_id, 1, 3, 'SYM_5'), (v_game_id, 1, 4, 'SYM_6'),
                                                                    (v_game_id, 1, 5, 'SYM_7'), (v_game_id, 1, 6, 'SYM_8'), (v_game_id, 1, 7, 'WILD'), (v_game_id, 1, 8, 'SCATTER'), (v_game_id, 1, 9, 'SYM_1');

-- Rodillo 2
INSERT INTO reel_strips (game_id, rodillo, posicion, symbol_id) VALUES
                                                                    (v_game_id, 2, 0, 'SYM_3'), (v_game_id, 2, 1, 'SYM_4'), (v_game_id, 2, 2, 'SYM_5'), (v_game_id, 2, 3, 'SYM_6'), (v_game_id, 2, 4, 'SYM_7'),
                                                                    (v_game_id, 2, 5, 'SYM_8'), (v_game_id, 2, 6, 'WILD'), (v_game_id, 2, 7, 'SCATTER'), (v_game_id, 2, 8, 'SYM_1'), (v_game_id, 2, 9, 'SYM_2');

-- Rodillo 3
INSERT INTO reel_strips (game_id, rodillo, posicion, symbol_id) VALUES
                                                                    (v_game_id, 3, 0, 'SYM_4'), (v_game_id, 3, 1, 'SYM_5'), (v_game_id, 3, 2, 'SYM_6'), (v_game_id, 3, 3, 'SYM_7'), (v_game_id, 3, 4, 'SYM_8'),
                                                                    (v_game_id, 3, 5, 'WILD'), (v_game_id, 3, 6, 'SCATTER'), (v_game_id, 3, 7, 'SYM_1'), (v_game_id, 3, 8, 'SYM_2'), (v_game_id, 3, 9, 'SYM_3');

-- Rodillo 4
INSERT INTO reel_strips (game_id, rodillo, posicion, symbol_id) VALUES
                                                                    (v_game_id, 4, 0, 'SYM_5'), (v_game_id, 4, 1, 'SYM_6'), (v_game_id, 4, 2, 'SYM_7'), (v_game_id, 4, 3, 'SYM_8'), (v_game_id, 4, 4, 'WILD'),
                                                                    (v_game_id, 4, 5, 'SCATTER'), (v_game_id, 4, 6, 'SYM_1'), (v_game_id, 4, 7, 'SYM_2'), (v_game_id, 4, 8, 'SYM_3'), (v_game_id, 4, 9, 'SYM_4');
END $$;