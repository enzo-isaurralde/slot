-- ==========================================
-- ETAPA 5.1: Crear tabla de usuarios
-- ==========================================
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(20) NOT NULL DEFAULT 'PLAYER',
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);