package com.vlprof.banking.service;

import com.vlprof.banking.dto.AccountDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountService {
    List<AccountDto> findAll();

    Optional<AccountDto> findById(UUID id);

    AccountDto create(AccountDto accountDto);
}
