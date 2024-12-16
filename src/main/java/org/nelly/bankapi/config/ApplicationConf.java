package org.nelly.bankapi.config;

import lombok.RequiredArgsConstructor;
import org.nelly.bankapi.models.Customer;
import org.nelly.bankapi.models.User;
import org.nelly.bankapi.repositories.CustomerRepository;
import org.nelly.bankapi.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ApplicationConf implements UserDetailsService {
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user;
        try {
            user = userRepository.findByEmailIgnoreCaseOrUsername(username, username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                    .orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));
        } catch (UsernameNotFoundException e) {
            user = customerRepository.findByEmailIgnoreCaseOrUsername(username, username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        }

        if (!user.isEnabled()) {
            throw new IllegalStateException("User account is not active. Contact the admin.");
        }
        return user;
    }

}
