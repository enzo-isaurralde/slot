-- ==========================================
-- ETAPA 6.1: Tabla de giros
-- ==========================================

CREATE TABLE spins (
                       id BIGSERIAL PRIMARY KEY,
                       user_id BIGINT NOT NULL REFERENCES users(id),
                       game_id BIGINT NOT NULL REFERENCES games(id),
                       bet_amount NUMERIC(19,2) NOT NULL,
                       win_amount NUMERIC(19,2) NOT NULL,
                       grid_json TEXT NOT NULL,
                       is_bonus BOOLEAN NOT NULL DEFAULT FALSE,
                       balance_after BIGINT NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);