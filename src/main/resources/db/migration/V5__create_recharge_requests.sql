-- ==========================================
-- ETAPA 5.5: Tabla de pedidos de recarga
-- ==========================================

CREATE TABLE recharge_requests (
                                   id BIGSERIAL PRIMARY KEY,
                                   user_id BIGINT NOT NULL REFERENCES users(id),
                                   amount BIGINT NOT NULL,
                                   status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
                                   approved_by BIGINT REFERENCES users(id),
                                   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   approved_at TIMESTAMP
);