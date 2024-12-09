CREATE TABLE IF NOT EXISTS groups (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        admin_id BIGINT NOT NULL,
                        FOREIGN KEY (admin_id) REFERENCES users(id)
);
