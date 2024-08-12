package org.syantovich.wbpublic.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.syantovich.wbpublic.domain.AccessToken;
import org.syantovich.wbpublic.domain.RefreshToken;
import org.syantovich.wbpublic.dto.AuthResponseDto;

public interface TokenService {
    AccessToken createToken(UserDetails userDetails, RefreshToken refreshToken);

    RefreshToken createRefreshToken(UserDetails userDetails);

    Authentication parseToken(String token);

    void validateToken(String token);

    void validateRefreshToken(String token);

    AuthResponseDto refresh(String refreshToken);

    void invalidate(String accessToken);
}
