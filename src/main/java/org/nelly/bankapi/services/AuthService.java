package org.nelly.bankapi.services;

import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import org.nelly.bankapi.config.JwtService;
import org.nelly.bankapi.repositories.CustomerRepository;
import org.nelly.bankapi.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService service;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final AuthenticationManager authenticationManager;

    public JSONObject login(JSONObject request) {

        String email = request.getString("email");
        String password = request.getString("password");

        UserDetails user;
        try {
            user = userRepository.findByEmailIgnoreCaseOrUsername(email, email)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                    .orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));
        } catch (UsernameNotFoundException e) {
            user = customerRepository.findByEmailIgnoreCaseOrUsername(email, email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        password));


        JSONObject claims = new JSONObject();

        var accessToken = service.generateToken(claims, user);
        var refreshToken = service.generateRefreshToken(claims, user);
        return JSONObject.of("accessToken", accessToken, "refreshToken", refreshToken);

    }
}
