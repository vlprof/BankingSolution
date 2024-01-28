CREATE TABLE account
(
    balance numeric(38, 2) NOT NULL,
    version INTEGER,
    created TIMESTAMP(6)   NOT NULL,
    updated TIMESTAMP(6)   NOT NULL,
    id      UUID           NOT NULL,
    CONSTRAINT ACCOUNT_PK PRIMARY KEY (id),
    CONSTRAINT ACCOUNT_BALANCE_CHECK CHECK (balance >= 0)
);