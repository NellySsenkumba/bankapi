package org.nelly.bankapi.repositories;

import org.nelly.bankapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailIgnoreCaseOrUsername(String email, String username);

    Optional<User> findUserByUsername(String username);
}
