package org.syantovich.wbpublic.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import org.syantovich.wbpublic.errors.InvalidTokenException;
import org.syantovich.wbpublic.services.TokenService;
import org.syantovich.wbpublic.services.impl.TokenServiceImpl;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class TokenAuthFilter extends OncePerRequestFilter {
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String bearAuth = request.getHeader("Authorization");
        if (bearAuth != null && bearAuth.startsWith("Bearer ")) {
            String token = bearAuth.substring(7);
            try {
                tokenService.validateToken(token);
                Authentication authentication = tokenService.parseToken(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (InvalidTokenException ignored) {

            }
        }

        filterChain.doFilter(request, response);
    }
}
