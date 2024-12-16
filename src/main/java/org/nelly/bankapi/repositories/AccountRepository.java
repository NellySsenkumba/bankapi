package org.nelly.bankapi.repositories;

import org.nelly.bankapi.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findAccountByAccountNumber(Long accountNumber);
}
