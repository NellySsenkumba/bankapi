package org.nelly.bankapi.config;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
@Log4j2
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper mapper;


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        log.debug("{}:{}", request.getMethod(), request.getRequestURI());
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userTag;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        try {
            log.debug("JWT Authentication Check");
            userTag = jwtService.extractUsername(jwt);
            if (userTag != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userTag);
                if (jwtService.extractType(jwt).equals("access") && jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        } catch (Exception e) {
            final JSONObject errorDetails = new JSONObject();
            errorDetails.put("code", HttpStatus.UNAUTHORIZED.value());
            errorDetails.put("message", "Invalid Token");
            if (e instanceof ExpiredJwtException) {
                errorDetails.put("message", "Expired Token");
            }
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(String.valueOf(MediaType.APPLICATION_JSON));
            mapper.writeValue(response.getWriter(), errorDetails);
            log.error(e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }
}
