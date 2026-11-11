-- ==========================================
-- ETAPA 2.2: Cargar tema militar por migración
-- ==========================================

-- 1. Insertar el juego
INSERT INTO games (codigo, nombre, rtp_objetivo)
VALUES ('SLOT_MILITAR', 'Slot Rat - Tema Militar', 95.00);

-- 2. Insertar los 10 símbolos (8 normales, 1 Wild, 1 Scatter)
INSERT INTO game_symbols (game_id, symbol_id, rol, asset_key) VALUES
                                                                  ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 'SYM_1', 'NORMAL', 'botas'),
                                                                  ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 'SYM_2', 'NORMAL', 'cantimplora'),
                                                                  ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 'SYM_3', 'NORMAL', 'cuchillo'),
                                                                  ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 'SYM_4', 'NORMAL', 'mapa'),
                                                                  ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 'SYM_5', 'NORMAL', 'brujula'),
                                                                  ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 'SYM_6', 'NORMAL', 'granada'),
                                                                  ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 'SYM_7', 'NORMAL', 'casco'),
                                                                  ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 'SYM_8', 'NORMAL', 'tanque'),
                                                                  ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 'WILD', 'WILD', 'medalla'),
                                                                  ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 'SCATTER', 'SCATTER', 'estrella');

-- 3. Las 10 Líneas de Pago (según el PDF)
INSERT INTO paylines (game_id, numero, r1, r2, r3, r4, r5) VALUES
                                                               ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 1, 1, 1, 1, 1, 1),
                                                               ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 2, 0, 0, 0, 0, 0),
                                                               ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 3, 2, 2, 2, 2, 2),
                                                               ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 4, 0, 1, 2, 1, 0),
                                                               ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 5, 2, 1, 0, 1, 2),
                                                               ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 6, 0, 0, 1, 2, 2),
                                                               ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 7, 2, 2, 1, 0, 0),
                                                               ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 8, 1, 0, 0, 0, 1),
                                                               ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 9, 1, 2, 2, 2, 1),
                                                               ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 10, 0, 1, 1, 1, 0);

-- 4. Pagos iniciales (Ejemplo para los bajos y altos según el PDF, podés ajustarlos)
INSERT INTO paytable (game_id, symbol_id, cantidad, multiplicador) VALUES
-- SYM_1 (Botas)
((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 'SYM_1', 3, 2), ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 'SYM_1', 4, 5), ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 'SYM_1', 5, 10),
-- SYM_2 (Cantimplora)
((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 'SYM_2', 3, 2), ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 'SYM_2', 4, 5), ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 'SYM_2', 5, 10),
-- SYM_3 (Cuchillo)
((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 'SYM_3', 3, 3), ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 'SYM_3', 4, 8), ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 'SYM_3', 5, 15),
-- SYM_4 (Mapa)
((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 'SYM_4', 3, 3), ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 'SYM_4', 4, 8), ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 'SYM_4', 5, 15),
-- SYM_5 (Brújula)
((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 'SYM_5', 3, 4), ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 'SYM_5', 4, 10), ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 'SYM_5', 5, 20),
-- SYM_6 (Granada) - Alto
((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 'SYM_6', 3, 5), ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 'SYM_6', 4, 20), ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 'SYM_6', 5, 50),
-- SYM_7 (Casco) - Alto
((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 'SYM_7', 3, 10), ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 'SYM_7', 4, 40), ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 'SYM_7', 5, 100),
-- SYM_8 (Tanque) - Alto (El que más paga)
((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 'SYM_8', 3, 15), ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 'SYM_8', 4, 60), ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 'SYM_8', 5, 200);

-- 5. Configuración del Bonus (3 Scatters = 10 giros, Multiplicador x2)
INSERT INTO bonus_config (game_id, tipo, scatters_requeridos, giros, multiplicador, permite_retrigger) VALUES
                                                                                                           ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 'FREE_SPINS', 3, 10, 2.00, TRUE),
                                                                                                           ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 'FREE_SPINS', 4, 15, 2.00, TRUE), -- Podés ajustar esto según lo que decidas
                                                                                                           ((SELECT id FROM games WHERE codigo = 'SLOT_MILITAR'), 'FREE_SPINS', 5, 20, 2.00, TRUE);