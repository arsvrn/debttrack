CREATE TABLE IF NOT EXISTS notifications
(
    id      SERIAL PRIMARY KEY,
    user_id BIGINT      NOT NULL,
    message TEXT,
    status  VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    sent_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id)
);
