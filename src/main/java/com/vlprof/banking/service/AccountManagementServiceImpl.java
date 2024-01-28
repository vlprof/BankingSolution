package com.vlprof.banking.service;

import com.vlprof.banking.dto.AccountRequestDto;
import com.vlprof.banking.dto.AccountResponseDto;
import com.vlprof.banking.dto.TransactionRequestDto;
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
public class AccountManagementServiceImpl implements AccountManagementService {
    private final AccountRepository repository;
    private final AccountTransactionsService transactionsService;

    @Override
    public List<AccountResponseDto> findAll() {
        var accounts = repository.findAllAsDto();
        if (accounts.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return accounts;
    }

    @Override
    public Optional<AccountResponseDto> findById(UUID id) {
        var account = repository.findByIdAsDto(id);
        if (account.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return account;
    }

    @Override
    public AccountResponseDto create(AccountRequestDto accountDto) {
        var account = new Account();
        account.setBalance(BigDecimal.ZERO);
        repository.save(account);
        if (accountDto != null && accountDto.balance() != null) {
            updateAccountBalance(account, accountDto.balance());
        }
        return new AccountResponseDto(account);
    }

    private void updateAccountBalance(Account account, BigDecimal balance) {
        transactionsService.deposit(TransactionRequestDto.builder()
                .accountId(account.getId())
                .amount(balance)
                .build()
        );
    }
}
