package com.vlprof.banking.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record AccountRequestDto(UUID id, BigDecimal balance) {
}
