package com.vlprof.banking.controller.rest;

import com.vlprof.banking.dto.TransactionRequestDto;
import com.vlprof.banking.dto.TransferRequestDto;
import com.vlprof.banking.service.AccountTransactionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Object> deposit(@RequestBody TransactionRequestDto transaction) {
        service.deposit(transaction);
        return ok();
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Object> withdraw(@RequestBody TransactionRequestDto transaction) {
        service.withdraw(transaction);
        return ok();
    }

    @PostMapping("/transfer")
    public ResponseEntity<Object> deposit(@RequestBody TransferRequestDto transfer) {
        service.transfer(transfer);
        return ok();
    }

    private static ResponseEntity<Object> ok() {
        return ResponseEntity.ok().build();
    }
}
