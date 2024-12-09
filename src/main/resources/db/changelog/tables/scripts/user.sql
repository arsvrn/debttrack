CREATE TABLE IF NOT EXISTS users
(
    id                    SERIAL PRIMARY KEY,
    email                 VARCHAR(255) NOT NULL UNIQUE,
    password_hash         VARCHAR(255) NOT NULL,
    name                  VARCHAR(255),
    role                  VARCHAR(50),
    enabled               BOOLEAN      NOT NULL DEFAULT FALSE,
    notifications_enabled BOOLEAN      NOT NULL DEFAULT TRUE
);
