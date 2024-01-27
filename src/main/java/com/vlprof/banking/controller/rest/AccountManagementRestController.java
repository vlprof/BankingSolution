package com.vlprof.banking.controller.rest;

import com.vlprof.banking.dto.AccountRequestDto;
import com.vlprof.banking.dto.AccountResponseDto;
import com.vlprof.banking.service.AccountManagementServiceImpl;
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
    private final AccountManagementServiceImpl service;

    @GetMapping
    public List<AccountResponseDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Optional<AccountResponseDto> find(@PathVariable("id") UUID id) {
        return service.findById(id);
    }

    @PostMapping
    public AccountResponseDto create(@RequestBody AccountRequestDto account) {
        return service.create(account);
    }
}
