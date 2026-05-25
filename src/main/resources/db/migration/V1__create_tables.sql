-- Users
CREATE TABLE users (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    email      VARCHAR(100) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    created_at DATETIME     NOT NULL
);

-- Wallets
CREATE TABLE wallets (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    balance    DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
    created_at DATETIME       NOT NULL,
    updated_at DATETIME       NOT NULL,
    user_id    BIGINT         NOT NULL UNIQUE,
    CONSTRAINT fk_wallet_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Transactions
CREATE TABLE transactions (
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    amount             DECIMAL(19, 2)  NOT NULL,
    type               VARCHAR(20)     NOT NULL,
    status             VARCHAR(20)     NOT NULL,
    sender_wallet_id   BIGINT,
    receiver_wallet_id BIGINT          NOT NULL,
    created_at         DATETIME        NOT NULL,
    CONSTRAINT fk_transaction_sender   FOREIGN KEY (sender_wallet_id)   REFERENCES wallets(id),
    CONSTRAINT fk_transaction_receiver FOREIGN KEY (receiver_wallet_id) REFERENCES wallets(id)
);

-- Transaction History (without UNIQUE on transaction_id, matching V4 + V5)
CREATE TABLE transaction_history (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    amount         DECIMAL(19, 2) NOT NULL,
    type           VARCHAR(20)    NOT NULL,
    status         VARCHAR(20)    NOT NULL,
    wallet_id      BIGINT         NOT NULL,
    transaction_id BIGINT         NOT NULL,
    archived_at    DATETIME       NOT NULL,
    CONSTRAINT fk_history_wallet      FOREIGN KEY (wallet_id)      REFERENCES wallets(id),
    CONSTRAINT fk_history_transaction FOREIGN KEY (transaction_id) REFERENCES transactions(id)
);