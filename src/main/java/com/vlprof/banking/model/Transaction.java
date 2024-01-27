package com.vlprof.banking.model;

import com.vlprof.banking.model.enums.TransactionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "transaction")
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "type", nullable = false)
    private TransactionType type;

    @Column(name = "time", nullable = false)
    private LocalDateTime time = LocalDateTime.now();

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @Builder
    public Transaction(TransactionType type, BigDecimal amount, UUID accountId) {
        this.type = type;
        this.amount = amount;
        this.accountId = accountId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction transaction = (Transaction) o;
        return id != null && id.equals(transaction.id);
    }

    @Override
    public int hashCode() {
        return Transaction.class.hashCode();
    }
}
