package com.vlprof.banking.repository;

import com.vlprof.banking.dto.AccountDto;
import com.vlprof.banking.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    @Query("select new com.vlprof.banking.dto.AccountDto(account) from Account account")
    List<AccountDto> findAllAsDto();

    @Query("select new com.vlprof.banking.dto.AccountDto(account) from Account account where account.id = :id")
    Optional<AccountDto> findByIdAsDto(@Param("id") UUID id);
}
