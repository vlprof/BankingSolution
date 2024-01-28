package com.vlprof.banking.dto;

import com.vlprof.banking.model.Account;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record AccountResponseDto(UUID id, BigDecimal balance, LocalDateTime created, LocalDateTime updated) {
    public AccountResponseDto(Account account) {
        this(account.getId(), account.getBalance(), account.getCreated(), account.getUpdated());
    }
}
