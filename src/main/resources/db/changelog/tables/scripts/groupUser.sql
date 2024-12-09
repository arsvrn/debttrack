CREATE TABLE IF NOT EXISTS group_users
(
    id       SERIAL PRIMARY KEY,
    group_id BIGINT NOT NULL,
    user_id  BIGINT NOT NULL,
    FOREIGN KEY (group_id) REFERENCES groups (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);
