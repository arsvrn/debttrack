CREATE TABLE if not exists confirmation_tokens
(
    id           SERIAL PRIMARY KEY,
    token        VARCHAR(255) NOT NULL UNIQUE,
    created_at   TIMESTAMP    NOT NULL,
    expires_at   TIMESTAMP    NOT NULL,
    confirmed_at TIMESTAMP,
    user_id      BIGINT       NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
);
