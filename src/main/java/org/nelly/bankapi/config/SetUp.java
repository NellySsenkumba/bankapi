package org.nelly.bankapi.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.nelly.bankapi.models.Role;
import org.nelly.bankapi.models.User;
import org.nelly.bankapi.repositories.RoleRepository;
import org.nelly.bankapi.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class SetUp {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @PostConstruct
    public void setUpUsers() {
        Role role = roleRepository.findRoleByCode("ADMIN")
                .orElseGet(() -> roleRepository.save(Role.builder()
                        .code("ADMIN")
                        .name("Admin")
                        .build()));
        userRepository.findUserByUsername("admin")
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .username("admin")
                                .password(passwordEncoder.encode("admin"))
                                .role(role)
                                .build()));
    }

}
