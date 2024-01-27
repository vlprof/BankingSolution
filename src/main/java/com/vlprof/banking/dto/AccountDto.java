package com.vlprof.banking.dto;

import com.vlprof.banking.model.Account;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountDto(UUID id, BigDecimal balance) {
    public AccountDto(Account account) {
        this(account.getId(), account.getBalance());
    }
}
