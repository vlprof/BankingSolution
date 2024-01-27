package com.vlprof.banking.service;

import com.vlprof.banking.dto.AccountDto;
import com.vlprof.banking.model.Account;
import com.vlprof.banking.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository repository;

    @Override
    public List<AccountDto> findAll() {
        var accounts = repository.findAllAsDto();
        if (accounts.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return accounts;
    }

    @Override
    public Optional<AccountDto> findById(UUID id) {
        var account = repository.findByIdAsDto(id);
        if (account.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return account;
    }

    @Override
    public AccountDto create(AccountDto accountDto) {
        var account = new Account();
        account.setBalance(getNonNullBalance(accountDto));
        repository.save(account);
        return new AccountDto(account);
    }

    private BigDecimal getNonNullBalance(AccountDto accountDto) {
        return accountDto.balance() != null ? accountDto.balance() : new BigDecimal(0);
    }
}
