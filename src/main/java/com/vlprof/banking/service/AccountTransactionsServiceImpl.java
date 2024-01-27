package com.vlprof.banking.service;

import com.vlprof.banking.dto.TransactionRequestDto;
import com.vlprof.banking.dto.TransferRequestDto;
import com.vlprof.banking.exception.service.AccountNotFoundException;
import com.vlprof.banking.model.Account;
import com.vlprof.banking.model.Transaction;
import com.vlprof.banking.model.enums.TransactionType;
import com.vlprof.banking.repository.AccountRepository;
import com.vlprof.banking.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class AccountTransactionsServiceImpl implements AccountTransactionsService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public void deposit(TransactionRequestDto transactionDto) {
        var transaction = createDepositTransaction(transactionDto);
        performDeposit(transaction);
    }

    @Override
    @Transactional
    public void withdraw(TransactionRequestDto transactionDto) {
        var transaction = createWithdrawalTransaction(transactionDto);
        performWithdrawal(transaction);
    }

    @Override
    @Transactional
    public void transfer(TransferRequestDto transfer) {
        var toTransaction = convertToDepositTransaction(transfer);
        var fromTransaction = convertToWithdrawalTransaction(transfer);
        performDeposit(toTransaction);
        performWithdrawal(fromTransaction);
    }

    private void performDeposit(Transaction transaction) {
        transactionRepository.save(transaction);
        updateAccount(transaction.getAccountId(), account ->
                account.setBalance(account.getBalance().add(transaction.getAmount()))
        );
    }

    private void performWithdrawal(Transaction transaction) {
        transactionRepository.save(transaction);
        updateAccount(transaction.getAccountId(), account ->
                account.setBalance(account.getBalance().subtract(transaction.getAmount()))
        );
    }

    private void updateAccount(UUID accountId, Consumer<Account> modifyAccountAction) {
        accountRepository.findByIdLock(accountId).ifPresentOrElse(
                account -> {
                    modifyAccountAction.accept(account);
                    accountRepository.save(account);
                    accountRepository.flush();
                }, () -> throwAccountNotFoundException(accountId)
        );
    }

    private Transaction createDepositTransaction(TransactionRequestDto transactionDto) {
        return createTransaction(transactionDto)
                .type(TransactionType.DEPOSIT).build();
    }

    private Transaction createWithdrawalTransaction(TransactionRequestDto transactionDto) {
        return createTransaction(transactionDto)
                .type(TransactionType.WITHDRAW).build();
    }

    private Transaction convertToDepositTransaction(TransferRequestDto transfer) {
        return createDepositTransaction(TransactionRequestDto
                .builder()
                .accountId(transfer.getTo())
                .amount(transfer.getAmount())
                .build()
        );
    }

    private Transaction convertToWithdrawalTransaction(TransferRequestDto transfer) {
        return createWithdrawalTransaction(TransactionRequestDto
                .builder()
                .accountId(transfer.getFrom())
                .amount(transfer.getAmount())
                .build()
        );
    }

    private Transaction.TransactionBuilder createTransaction(TransactionRequestDto transactionDto) {
        var accountId = transactionDto.getAccountId();
        checkIfAccountExists(accountId);
        return Transaction.builder()
                .accountId(accountId)
                .amount(transactionDto.getAmount());
    }

    private void checkIfAccountExists(UUID accountId) {
        if (!accountRepository.existsById(accountId)) {
            throwAccountNotFoundException(accountId);
        }
    }

    private void throwAccountNotFoundException(UUID accountId) {
        throw new AccountNotFoundException(accountId);
    }
}
