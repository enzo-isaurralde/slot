-- ==========================================
-- ETAPA 2.1: Entidades y migración
-- ==========================================

-- Tabla principal de juegos (temas)
CREATE TABLE games (
                       id BIGSERIAL PRIMARY KEY,
                       codigo VARCHAR(50) UNIQUE NOT NULL,
                       nombre VARCHAR(100) NOT NULL,
                       version_config INT NOT NULL DEFAULT 1,
                       rtp_objetivo NUMERIC(5,2),
                       activo BOOLEAN NOT NULL DEFAULT TRUE,
                       creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Símbolos por juego (Militar, Nacional, etc.)
CREATE TABLE game_symbols (
                              id BIGSERIAL PRIMARY KEY,
                              game_id BIGINT NOT NULL REFERENCES games(id) ON DELETE CASCADE,
                              symbol_id VARCHAR(20) NOT NULL, -- SYM_1, WILD, SCATTER
                              rol VARCHAR(20) NOT NULL,       -- NORMAL, WILD, SCATTER
                              asset_key VARCHAR(100) NOT NULL -- Nombre del archivo de imagen
);

-- Cintas de los rodillos (Reel Strips)
CREATE TABLE reel_strips (
                             id BIGSERIAL PRIMARY KEY,
                             game_id BIGINT NOT NULL REFERENCES games(id) ON DELETE CASCADE,
                             rodillo INT NOT NULL,           -- 0 a 4 (los 5 rodillos)
                             posicion INT NOT NULL,          -- Posición en la cinta
                             symbol_id VARCHAR(20) NOT NULL  -- SYM_1, etc.
);

-- Líneas de pago (10 líneas)
CREATE TABLE paylines (
                          id BIGSERIAL PRIMARY KEY,
                          game_id BIGINT NOT NULL REFERENCES games(id) ON DELETE CASCADE,
                          numero INT NOT NULL,            -- Línea 1 a 10
                          r1 INT NOT NULL,                -- Fila del rodillo 1 (0, 1, 2)
                          r2 INT NOT NULL,
                          r3 INT NOT NULL,
                          r4 INT NOT NULL,
                          r5 INT NOT NULL
);

-- Tabla de pagos
CREATE TABLE paytable (
                          id BIGSERIAL PRIMARY KEY,
                          game_id BIGINT NOT NULL REFERENCES games(id) ON DELETE CASCADE,
                          symbol_id VARCHAR(20) NOT NULL,
                          cantidad INT NOT NULL,          -- 3, 4 o 5
                          multiplicador NUMERIC(10,2) NOT NULL
);

-- Configuración del Bonus
CREATE TABLE bonus_config (
                              id BIGSERIAL PRIMARY KEY,
                              game_id BIGINT NOT NULL REFERENCES games(id) ON DELETE CASCADE,
                              tipo VARCHAR(30) NOT NULL,      -- FREE_SPINS
                              scatters_requeridos INT NOT NULL,
                              giros INT NOT NULL,             -- Giros otorgados
                              multiplicador NUMERIC(5,2) NOT NULL,
                              permite_retrigger BOOLEAN DEFAULT TRUE
);