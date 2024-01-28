create table transaction
(
    amount     NUMERIC(38, 2) not null,
    type       SMALLINT       not null check (type between 0 and 1),
    time       TIMESTAMP(6)   not null,
    account_id UUID           not null,
    id         UUID           not null,
    CONSTRAINT TRANSACTION_PK PRIMARY KEY (id),
    CONSTRAINT TRANSACTION_ACCOUNT_FK FOREIGN KEY (account_id) REFERENCES account(id)
)
