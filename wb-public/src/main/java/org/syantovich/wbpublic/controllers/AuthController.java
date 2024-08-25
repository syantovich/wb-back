package org.syantovich.wbpublic.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.syantovich.wbpublic.dto.AuthResponseDto;
import org.syantovich.wbpublic.dto.LoginDto;
import org.syantovich.wbpublic.dto.RegisterDto;
import org.syantovich.wbpublic.services.AuthService;
import org.syantovich.wbpublic.services.TokenService;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/auth")
public class AuthController {
    private final AuthService authService;
    private final TokenService tokenService;

    @PostMapping("/register")
    public AuthResponseDto registerPerson(@RequestBody RegisterDto registerDto) {
        return authService.registerPerson(registerDto);
    }

    @PostMapping("/login")
    public AuthResponseDto login(@RequestBody LoginDto loginDto) {
        return authService.login(loginDto);
    }

    @GetMapping("/refresh")
    public AuthResponseDto refresh(@RequestHeader(name = "X-Refresh-token") String refreshToken) {
        return tokenService.refresh(refreshToken);
    }

    @PostMapping("/logout")
    public void logout(@RequestHeader(name = "Authorization") String accessToken) {
        tokenService.invalidate(accessToken);
    }

    @GetMapping("/me")
    public AuthResponseDto getMe(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authService.getMe(authentication.getName());
    }
}
