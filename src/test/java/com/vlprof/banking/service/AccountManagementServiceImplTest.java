package com.vlprof.banking.service;

import com.vlprof.banking.dto.AccountRequestDto;
import com.vlprof.banking.dto.AccountResponseDto;
import com.vlprof.banking.dto.TransactionRequestDto;
import com.vlprof.banking.model.Account;
import com.vlprof.banking.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountManagementServiceImplTest {
    @InjectMocks
    private AccountManagementServiceImpl accountManagementService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountTransactionsService accountTransactionsService;

    private AccountResponseDto accountDto;

    @BeforeEach
    public void init() {
        accountDto = AccountResponseDto.builder()
                .id(UUID.randomUUID())
                .balance(BigDecimal.valueOf(100))
                .created(LocalDateTime.now().minusHours(1))
                .updated(LocalDateTime.now())
                .build();
    }

    @Test
    void findAll_emptyRepository_exception() {
        assertThrows(ResponseStatusException.class, () -> accountManagementService.findAll());
    }

    @Test
    void findAll_repositoryWithSingleEntity_listWithEntityDto() {
        when(accountRepository.findAllAsDto()).thenReturn(List.of(accountDto));
        var expected = List.of(accountDto);

        var result = accountManagementService.findAll();

        assertEquals(expected, result);
    }

    @Test
    void findById_unknownAccount_exception() {
        var accountId = UUID.randomUUID();
        assertThrows(ResponseStatusException.class, () -> accountManagementService.findById(accountId));
    }

    @Test
    void findById_knownAccount_accountDto() {
        when(accountRepository.findByIdAsDto(accountDto.id())).thenReturn(Optional.of(accountDto));

        var result = accountManagementService.findById(accountDto.id());

        assertEquals(accountDto, result.get());
    }

    @Test
    void create_emptyRequest_accountDto() {
        var accountId = UUID.randomUUID();
        mockAccountRepositorySaveMethod(accountId);

        var result = accountManagementService.create(AccountRequestDto.builder().build());

        assertAll(
                () -> verify(accountRepository).save(any(Account.class)),
                () -> verifyNoInteractions(accountTransactionsService),
                () -> assertEquals(accountId, result.id()),
                () -> assertEquals(BigDecimal.ZERO, result.balance())
        );
    }

    @Test
    void create_requestWithUUIDAndBalance_accountDto() {
        var accountId = UUID.randomUUID();
        var initialBalance = BigDecimal.valueOf(100);
        mockAccountRepositorySaveMethod(accountId);

        var result = accountManagementService.create(AccountRequestDto.builder()
                .id(UUID.randomUUID())
                .balance(initialBalance)
                .build()
        );

        assertAll(
                () -> verify(accountRepository).save(any(Account.class)),
                () -> verify(accountTransactionsService).deposit(TransactionRequestDto.builder()
                        .accountId(accountId)
                        .amount(initialBalance)
                        .build()
                ),
                () -> assertEquals(accountId, result.id()),
                () -> assertEquals(BigDecimal.ZERO, result.balance())
        );
    }

    private void mockAccountRepositorySaveMethod(UUID accountId) {
        when(accountRepository.save(any(Account.class))).then(invocation -> {
            Account account = invocation.getArgument(0);
            account.setId(accountId);
            return account;
        });
    }
}