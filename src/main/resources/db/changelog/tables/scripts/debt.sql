CREATE TABLE IF NOT EXISTS debts
(
    id             SERIAL PRIMARY KEY,
    creditor_id    BIGINT         NOT NULL,
    borrower_id    BIGINT         NOT NULL,
    amount         NUMERIC(10, 2) NOT NULL,
    interest_rate  NUMERIC(5, 2)  NOT NULL,
    due_date       DATE           NOT NULL,
    note           TEXT,
    status         VARCHAR(50)    NOT NULL DEFAULT 'ACTIVE',
    total_amount   NUMERIC(10, 2) NOT NULL,
    penalty_amount NUMERIC(10, 2) NOT NULL DEFAULT 0.0,
    FOREIGN KEY (creditor_id) REFERENCES users (id),
    FOREIGN KEY (borrower_id) REFERENCES users (id)
);
