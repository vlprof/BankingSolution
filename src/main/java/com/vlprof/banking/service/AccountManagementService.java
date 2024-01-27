package com.vlprof.banking.service;

import com.vlprof.banking.dto.AccountRequestDto;
import com.vlprof.banking.dto.AccountResponseDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountManagementService {
    List<AccountResponseDto> findAll();

    Optional<AccountResponseDto> findById(UUID id);

    AccountResponseDto create(AccountRequestDto accountResponseDto);
}
