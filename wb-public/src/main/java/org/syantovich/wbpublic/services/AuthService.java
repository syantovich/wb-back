package org.syantovich.wbpublic.services;

import org.syantovich.wbpublic.dto.AuthResponseDto;
import org.syantovich.wbpublic.dto.LoginDto;
import org.syantovich.wbpublic.dto.RegisterDto;

public interface AuthService {
    AuthResponseDto registerPerson(RegisterDto registerDto);

    AuthResponseDto login(LoginDto loginDto);

    AuthResponseDto getMe(String username);
}
