package org.nelly.bankapi.repositories;

import org.nelly.bankapi.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findRoleByCode(String code);
}
