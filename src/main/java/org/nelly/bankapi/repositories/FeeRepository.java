package org.nelly.bankapi.repositories;

import org.nelly.bankapi.models.Fee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeeRepository extends JpaRepository<Fee, Long> {
}
