package com.backend.system.filter;

import com.backend.system.exception.ErrorCode;
import com.backend.system.exception.InvalidTokenException;
import com.backend.system.exception.TokenException;
import com.backend.system.security.JwtAuthenticationProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    JwtAuthenticationProvider jwtAuthenticationProvider;
    UserDetailsService userDetailsService;
    HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("Processing request: {} {}", request.getMethod(), request.getRequestURI());
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.contains("Bearer")) {
            log.info("Request does not contain Authorization header");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            log.info("Request contains Authorization header");
            String token = authHeader.substring(7);
            String username = jwtAuthenticationProvider.extractUserName(token);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (username != null && authentication == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtAuthenticationProvider.isTokenValid(token, userDetails)) {
                    log.info("Token is valid for user: {}", username);
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } else {
                    log.warn("Token is invalid for user: {}", username);
                    throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
                }
            }
            filterChain.doFilter(request, response);
        } catch (TokenException e) {
            log.error("Error processing JWT token: {}", e.getMessage());
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }

}
