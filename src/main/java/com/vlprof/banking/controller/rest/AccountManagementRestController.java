package com.vlprof.banking.controller.rest;

import com.vlprof.banking.dto.AccountDto;
import com.vlprof.banking.service.AccountServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountManagementRestController {
    private final AccountServiceImpl service;

    @GetMapping
    public List<AccountDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Optional<AccountDto> find(@PathVariable("id") UUID id) {
        return service.findById(id);
    }

    @PostMapping
    public AccountDto create(@RequestBody AccountDto accountDto) {
        return service.create(accountDto);
    }
}
