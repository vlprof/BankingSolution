package com.vlprof.banking.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@EqualsAndHashCode
public class TransactionRequestDto {
    private UUID accountId;

    private BigDecimal amount;
}
