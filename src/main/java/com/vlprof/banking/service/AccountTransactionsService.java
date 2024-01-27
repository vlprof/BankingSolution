package com.vlprof.banking.service;

import com.vlprof.banking.dto.TransactionRequestDto;
import com.vlprof.banking.dto.TransferRequestDto;

public interface AccountTransactionsService {
    void deposit(TransactionRequestDto requestDto);

    void withdraw(TransactionRequestDto requestDto);

    void transfer(TransferRequestDto transfer);
}
