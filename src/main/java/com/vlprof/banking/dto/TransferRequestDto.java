package com.vlprof.banking.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
public class TransferRequestDto {
    private UUID from;

    private UUID to;

    private BigDecimal amount;
}
