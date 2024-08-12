package org.syantovich.wbpublic.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.syantovich.wbpublic.enums.Authorities;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    @Value("${api.prefix}")
    private String apiPrefix;
    private final TokenAuthFilter tokenAuthFilter;

    @Bean
    SecurityFilterChain securityFilterChain
            (HttpSecurity http) throws Exception {

        return http
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
                    corsConfiguration.setAllowedOrigins(List.of("*"));
                    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfiguration.setAllowedHeaders(List.of("*"));
                    return corsConfiguration;
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(tokenAuthFilter, LogoutFilter.class)
                .authorizeRequests(authorizeRequests ->
                                authorizeRequests
                                        .requestMatchers(apiPrefix + "/auth/**").permitAll()
//                                .requestMatchers(apiPrefix + "/verification/send").hasAuthority(Authorities.UNVERIFIED.toString())
//                                .anyRequest().hasAnyAuthority(Authorities.VERIFIED.toString())
                                        .anyRequest().permitAll()
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .accessDeniedHandler(accessDeniedHandler())
                )
                .build();
    }

    @Bean
    AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.sendRedirect("/no-access");
        };
    }
}
