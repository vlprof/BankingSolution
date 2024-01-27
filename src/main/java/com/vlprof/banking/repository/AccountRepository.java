package com.vlprof.banking.repository;

import com.vlprof.banking.dto.AccountResponseDto;
import com.vlprof.banking.model.Account;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    @Query("select new com.vlprof.banking.dto.AccountResponseDto(account) from Account account")
    List<AccountResponseDto> findAllAsDto();

    @Query("select new com.vlprof.banking.dto.AccountResponseDto(account) from Account account where account.id = :id")
    Optional<AccountResponseDto> findByIdAsDto(@Param("id") UUID id);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("select account from Account account where account.id = :id")
    Optional<Account> findByIdLock(@Param("id") UUID id);
}
