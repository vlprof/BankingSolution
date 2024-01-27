package com.vlprof.banking.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountRequestDto(UUID id, BigDecimal balance) {
}
