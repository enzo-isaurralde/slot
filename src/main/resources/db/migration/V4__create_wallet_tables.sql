-- ==========================================
-- ETAPA 5.3: Tablas de Billetera
-- ==========================================

CREATE TABLE wallets (
                         id BIGSERIAL PRIMARY KEY,
                         user_id BIGINT UNIQUE NOT NULL REFERENCES users(id),
                         balance BIGINT NOT NULL DEFAULT 10000, -- 100.00 pesos en centavos
                         version INT NOT NULL DEFAULT 0
);

CREATE TABLE wallet_transactions (
                                     id BIGSERIAL PRIMARY KEY,
                                     wallet_id BIGINT NOT NULL REFERENCES wallets(id),
                                     type VARCHAR(20) NOT NULL,
                                     amount BIGINT NOT NULL,
                                     balance_after BIGINT NOT NULL,
                                     reference VARCHAR(255),
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);