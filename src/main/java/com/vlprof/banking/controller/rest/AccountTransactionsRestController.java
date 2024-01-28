package com.vlprof.banking.controller.rest;

import com.vlprof.banking.dto.TransactionRequestDto;
import com.vlprof.banking.dto.TransactionResponseDto;
import com.vlprof.banking.dto.TransferRequestDto;
import com.vlprof.banking.service.AccountTransactionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class AccountTransactionsRestController {
    private final AccountTransactionsService service;

    @PostMapping("/deposit")
    public TransactionResponseDto deposit(@RequestBody TransactionRequestDto transaction) {
        service.deposit(transaction);
        return createResponseWithType("deposit");
    }

    @PostMapping("/withdraw")
    public TransactionResponseDto withdraw(@RequestBody TransactionRequestDto transaction) {
        service.withdraw(transaction);
        return createResponseWithType("withdraw");
    }

    @PostMapping("/transfer")
    public TransactionResponseDto transfer(@RequestBody TransferRequestDto transfer) {
        service.transfer(transfer);
        return createResponseWithType("transfer");
    }

    private TransactionResponseDto createResponseWithType(String type) {
        var message = type + " request has been completed";
        return new TransactionResponseDto(message);
    }
}
