CREATE TABLE bonus_sessions (
                                id BIGSERIAL PRIMARY KEY,
                                user_id BIGINT NOT NULL REFERENCES users(id),
                                game_id BIGINT NOT NULL REFERENCES games(id),
                                giros_restantes INT NOT NULL,
                                multiplicador NUMERIC(5,2) NOT NULL,
                                premio_acumulado NUMERIC(19,2) NOT NULL DEFAULT 0,
                                estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);