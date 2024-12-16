package org.nelly.bankapi.config;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.nelly.bankapi.models.Customer;
import org.nelly.bankapi.models.User;
import org.nelly.bankapi.repositories.CustomerRepository;
import org.nelly.bankapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.nelly.bankapi.exception.UnauthorisedException;
import org.nelly.bankapi.models.Role;

import javax.crypto.SecretKey;
import java.sql.Timestamp;
import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    @Value("${application.security.jwt.secret-key}")
    private String secret;

    @Value("${application.security.jwt.expiration}")
    private long webJwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long webRefreshExpiration;

    @Value("${application.security.jwt.expiration.mobile}")
    private long mobileJwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration.mobile}")
    private long mobileRefreshExpiration;


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractIssuedAt(String token) {
        return extractClaim(token, Claims::getIssuedAt);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) throws Exception {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token, username);
    }

    /**
     * @param token    jwt token
     * @param username username used when setting up userDetails, for our case, we use username
     * @return Boolean
     * @implNote Will check for two things, if the token is expired as per eat claim
     * and will also check of the user has already requested for another token, requesting for another token
     * makes all the previously generated token expired.
     */

    private boolean isTokenExpired(String token, String username) throws Exception {
        try {
            User userModel = userRepository.findByEmailIgnoreCaseOrUsername(username, username)
                    .orElseThrow(() -> new UnauthorisedException("Invalid Token - User not found"));
            if (userModel.getLastLoggedInAt() == null) {
                throw new UnauthorisedException("Invalid Token - User has never logged in");
            } else if (userModel.getLastLoggedInAt().toInstant().getEpochSecond() != extractIssuedAt(token).toInstant().getEpochSecond()) {
                throw new UnauthorisedException("Invalid Token - Old Token Used");
            } else if (!userModel.isEnabled()) {
                throw new UnauthorisedException("Invalid Token - Account Inactive");
            }
        } catch (UnauthorisedException e) {
            Customer userModel = customerRepository.findByEmailIgnoreCaseOrUsername(username, username)
                    .orElseThrow(() -> new UnauthorisedException("Invalid Token - User not found"));
            if (userModel.getLastLoggedInAt() == null) {
                throw new UnauthorisedException("Invalid Token - User has never logged in");
            } else if (userModel.getLastLoggedInAt().toInstant().getEpochSecond() != extractIssuedAt(token).toInstant().getEpochSecond()) {
                throw new UnauthorisedException("Invalid Token - Old Token Used");
            } else if (!userModel.isEnabled()) {
                throw new UnauthorisedException("Invalid Token - Account Inactive");
            }
        }

        return false;
    }


    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        // truck the last time the token was generated -- the last time the user logged in
        long now = System.currentTimeMillis();
        Timestamp stamp = new Timestamp(now);
        try {
            User user = (User) userDetails;

            // archived staff members should not login
            user.setLastLoggedInAt(stamp);
            userRepository.save(user);
        } catch (ClassCastException e) {
            Customer customer = (Customer) userDetails;
            customer.setLastLoggedInAt(stamp);
            customerRepository.save(customer);
        }


        claims.put("type", "access");
        List<String> permissions = new ArrayList<>();
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            if (!permissions.contains(authority.getAuthority())) {
                permissions.add(authority.getAuthority());
            }
        }

        return generateToken(claims, userDetails, webJwtExpiration);
    }

    public String generateRefreshToken(Map<String, Object> claims, UserDetails userDetails) {
        claims.put("type", "refresh");
        return generateToken(claims, userDetails, webRefreshExpiration);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractType(String token) {
        return extractClaim(token, claims -> claims.get("type", String.class));
    }


}
