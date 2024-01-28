package com.vlprof.banking.service;

import com.vlprof.banking.dto.TransactionRequestDto;
import com.vlprof.banking.dto.TransferRequestDto;
import com.vlprof.banking.exception.service.AccountNotFoundException;
import com.vlprof.banking.model.Account;
import com.vlprof.banking.model.Transaction;
import com.vlprof.banking.model.enums.TransactionType;
import com.vlprof.banking.repository.AccountRepository;
import com.vlprof.banking.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccountTransactionsServiceImplTest {
    @InjectMocks
    private AccountTransactionsServiceImpl accountTransactionsService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Captor
    private ArgumentCaptor<Transaction> transactionCaptor;

    private Account account;

    private Account fromAccount;

    private Account toAccount;

    @BeforeEach
    void init() {
        account = createAccount(100);
        fromAccount = createAccount(50);
        toAccount = createAccount(150);
    }

    @Test
    void deposit_unknownAccount_exception() {
        var request = TransactionRequestDto
                .builder()
                .accountId(UUID.randomUUID())
                .amount(BigDecimal.valueOf(100))
                .build();

        assertThrows(AccountNotFoundException.class, () -> accountTransactionsService.deposit(request));
    }

    @Test
    void deposit_accountWithInitialBalance_shouldIncreaseBalance() {
        var amount = BigDecimal.valueOf(10);
        mockAccountRepositoryToReturnAccount(account);

        accountTransactionsService.deposit(TransactionRequestDto
                .builder()
                .accountId(account.getId())
                .amount(amount)
                .build()
        );

        verify(transactionRepository).save(transactionCaptor.capture());
        assertAll(Stream.concat(
                assertAccountIsValid(account, 110),
                assertTransactionIsValid(transactionCaptor.getValue(), TransactionType.DEPOSIT, account.getId())
        ));
    }

    @Test
    void withdraw_unknownAccount_exception() {
        var request = TransactionRequestDto
                .builder()
                .accountId(UUID.randomUUID())
                .amount(BigDecimal.valueOf(100))
                .build();

        assertThrows(AccountNotFoundException.class, () -> accountTransactionsService.withdraw(request));
    }

    @Test
    void withdraw_accountWithInitialBalance_shouldDecreaseBalance() {
        mockAccountRepositoryToReturnAccount(account);

        accountTransactionsService.withdraw(TransactionRequestDto
                .builder()
                .accountId(account.getId())
                .amount(BigDecimal.valueOf(10))
                .build()
        );

        verify(transactionRepository).save(transactionCaptor.capture());
        assertAll(Stream.concat(
                assertAccountIsValid(account, 90),
                assertTransactionIsValid(transactionCaptor.getValue(), TransactionType.WITHDRAW, account.getId())
        ));
    }

    @Test
    void transfer_unknownFromAccount_exception() {
        var request = TransferRequestDto
                .builder()
                .from(fromAccount.getId())
                .to(toAccount.getId())
                .amount(BigDecimal.valueOf(100))
                .build();
        mockAccountRepositoryToReturnAccount(toAccount);

        assertThrows(AccountNotFoundException.class, () -> accountTransactionsService.transfer(request));
    }

    @Test
    void transfer_unknownToAccount_exception() {
        var request = TransferRequestDto
                .builder()
                .from(fromAccount.getId())
                .to(toAccount.getId())
                .amount(BigDecimal.valueOf(100))
                .build();
        mockAccountRepositoryToReturnAccount(fromAccount);

        assertThrows(AccountNotFoundException.class, () -> accountTransactionsService.transfer(request));
    }

    @Test
    void transfer_fromFirstToSecondAccount_shouldChangeBalanceOnBothAccounts() {
        mockAccountRepositoryToReturnAccount(fromAccount);
        mockAccountRepositoryToReturnAccount(toAccount);

        accountTransactionsService.transfer(TransferRequestDto
                .builder()
                .from(fromAccount.getId())
                .to(toAccount.getId())
                .amount(BigDecimal.valueOf(50))
                .build()
        );

        verify(transactionRepository, times(2)).save(transactionCaptor.capture());

        var depositToAccount = transactionCaptor.getAllValues().get(0);
        var withdrawFromAccount = transactionCaptor.getAllValues().get(1);

        assertAll(Stream.of(
                assertAccountIsValid(toAccount, 200),
                assertAccountIsValid(fromAccount, 0),
                assertTransactionIsValid(depositToAccount, TransactionType.DEPOSIT, toAccount.getId()),
                assertTransactionIsValid(withdrawFromAccount, TransactionType.WITHDRAW, fromAccount.getId())
        ).flatMap(Function.identity()));
    }

    private void mockAccountRepositoryToReturnAccount(Account account) {
        lenient().when(accountRepository.existsById(account.getId())).thenReturn(true);
        lenient().when(accountRepository.findByIdLock(account.getId())).thenReturn(Optional.of(account));
    }

    private Account createAccount(int balance) {
        return Account
                .builder()
                .id(UUID.randomUUID())
                .balance(BigDecimal.valueOf(balance))
                .build();
    }

    private Stream<Executable> assertTransactionIsValid(
            Transaction transaction,
            TransactionType transactionType,
            UUID accountId) {
        return Stream.of(
                () -> assertEquals(accountId, transaction.getAccountId()),
                () -> assertEquals(transactionType, transaction.getType())
        );
    }

    private Stream<Executable> assertAccountIsValid(Account account, int expectedBalance) {
        return Stream.of(
                () -> verify(accountRepository).save(account),
                () -> assertEquals(expectedBalance, account.getBalance().intValue())
        );
    }
}