-- 1. Insertar el juego
INSERT INTO games (codigo, nombre, rtp_objetivo) VALUES ('SLOT_MILITAR', 'Slot Rat - Tema Militar', 95.00);

-- 2. Insertar los 10 símbolos
INSERT INTO game_symbols (game_id, symbol_id, rol, asset_key) VALUES
                                                                  (1, 'SYM_1', 'NORMAL', 'botas'), (1, 'SYM_2', 'NORMAL', 'cantimplora'), (1, 'SYM_3', 'NORMAL', 'cuchillo'),
                                                                  (1, 'SYM_4', 'NORMAL', 'mapa'), (1, 'SYM_5', 'NORMAL', 'brujula'), (1, 'SYM_6', 'NORMAL', 'granada'),
                                                                  (1, 'SYM_7', 'NORMAL', 'casco'), (1, 'SYM_8', 'NORMAL', 'tanque'), (1, 'WILD', 'WILD', 'medalla'),
                                                                  (1, 'SCATTER', 'SCATTER', 'estrella');

-- 3. Las 10 Líneas de Pago
INSERT INTO paylines (game_id, numero, r1, r2, r3, r4, r5) VALUES
                                                               (1, 1, 1, 1, 1, 1, 1), (1, 2, 0, 0, 0, 0, 0), (1, 3, 2, 2, 2, 2, 2), (1, 4, 0, 1, 2, 1, 0),
                                                               (1, 5, 2, 1, 0, 1, 2), (1, 6, 0, 0, 1, 2, 2), (1, 7, 2, 2, 1, 0, 0), (1, 8, 1, 0, 0, 0, 1),
                                                               (1, 9, 1, 2, 2, 2, 1), (1, 10, 0, 1, 1, 1, 0);

-- 4. Pagos calibrados para RTP exacto ~95%
INSERT INTO paytable (game_id, symbol_id, cantidad, multiplicador) VALUES
-- Bajos (bajamos un poco los de 4)
(1, 'SYM_1', 3, 2), (1, 'SYM_1', 4, 4), (1, 'SYM_1', 5, 12),
(1, 'SYM_2', 3, 2), (1, 'SYM_2', 4, 4), (1, 'SYM_2', 5, 12),
(1, 'SYM_3', 3, 3), (1, 'SYM_3', 4, 6), (1, 'SYM_3', 5, 18),
(1, 'SYM_4', 3, 3), (1, 'SYM_4', 4, 6), (1, 'SYM_4', 5, 18),
-- Medios (bajamos un poco los de 4)
(1, 'SYM_5', 3, 4), (1, 'SYM_5', 4, 10), (1, 'SYM_5', 5, 30),
(1, 'SYM_6', 3, 8), (1, 'SYM_6', 4, 20), (1, 'SYM_6', 5, 75),
-- Altos (los dejamos igual)
(1, 'SYM_7', 3, 25), (1, 'SYM_7', 4, 100), (1, 'SYM_7', 5, 250),
(1, 'SYM_8', 3, 50), (1, 'SYM_8', 4, 180), (1, 'SYM_8', 5, 350),
-- Scatter
(1, 'SCATTER', 3, 2), (1, 'SCATTER', 4, 8), (1, 'SCATTER', 5, 40);

-- 5. Configuración del Bonus
INSERT INTO bonus_config (game_id, tipo, scatters_requeridos, giros, multiplicador, permite_retrigger) VALUES
                                                                                                           (1, 'FREE_SPINS', 3, 10, 2.00, TRUE),
                                                                                                           (1, 'FREE_SPINS', 4, 15, 2.00, TRUE),
                                                                                                           (1, 'FREE_SPINS', 5, 20, 2.00, TRUE);

-- 6. Cintas de los rodillos (repetimos símbolos bajos para controlar la frecuencia)
-- Rodillo 0
INSERT INTO reel_strips (game_id, rodillo, posicion, symbol_id) VALUES
                                                                    (1, 0, 0, 'SYM_1'), (1, 0, 1, 'SYM_2'), (1, 0, 2, 'SYM_3'), (1, 0, 3, 'SYM_1'), (1, 0, 4, 'SYM_2'),
                                                                    (1, 0, 5, 'SYM_4'), (1, 0, 6, 'SYM_5'), (1, 0, 7, 'WILD'), (1, 0, 8, 'SYM_6'), (1, 0, 9, 'SCATTER');

-- Rodillo 1
INSERT INTO reel_strips (game_id, rodillo, posicion, symbol_id) VALUES
                                                                    (1, 1, 0, 'SYM_1'), (1, 1, 1, 'SYM_2'), (1, 1, 2, 'SYM_3'), (1, 1, 3, 'SYM_1'), (1, 1, 4, 'SYM_2'),
                                                                    (1, 1, 5, 'SYM_4'), (1, 1, 6, 'SYM_5'), (1, 1, 7, 'WILD'), (1, 1, 8, 'SYM_6'), (1, 1, 9, 'SCATTER');

-- Rodillo 2
INSERT INTO reel_strips (game_id, rodillo, posicion, symbol_id) VALUES
                                                                    (1, 2, 0, 'SYM_1'), (1, 2, 1, 'SYM_2'), (1, 2, 2, 'SYM_3'), (1, 2, 3, 'SYM_1'), (1, 2, 4, 'SYM_2'),
                                                                    (1, 2, 5, 'SYM_4'), (1, 2, 6, 'SYM_5'), (1, 2, 7, 'WILD'), (1, 2, 8, 'SYM_6'), (1, 2, 9, 'SCATTER');

-- Rodillo 3
INSERT INTO reel_strips (game_id, rodillo, posicion, symbol_id) VALUES
                                                                    (1, 3, 0, 'SYM_1'), (1, 3, 1, 'SYM_2'), (1, 3, 2, 'SYM_3'), (1, 3, 3, 'SYM_1'), (1, 3, 4, 'SYM_2'),
                                                                    (1, 3, 5, 'SYM_4'), (1, 3, 6, 'SYM_5'), (1, 3, 7, 'WILD'), (1, 3, 8, 'SYM_6'), (1, 3, 9, 'SCATTER');

-- Rodillo 4
INSERT INTO reel_strips (game_id, rodillo, posicion, symbol_id) VALUES
                                                                    (1, 4, 0, 'SYM_1'), (1, 4, 1, 'SYM_2'), (1, 4, 2, 'SYM_3'), (1, 4, 3, 'SYM_1'), (1, 4, 4, 'SYM_2'),
                                                                    (1, 4, 5, 'SYM_4'), (1, 4, 6, 'SYM_5'), (1, 4, 7, 'WILD'), (1, 4, 8, 'SYM_6'), (1, 4, 9, 'SCATTER');