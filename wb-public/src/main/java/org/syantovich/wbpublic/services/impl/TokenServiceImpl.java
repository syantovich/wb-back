package org.syantovich.wbpublic.services.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.syantovich.wbpublic.domain.AccessToken;
import org.syantovich.wbpublic.domain.RefreshToken;
import org.syantovich.wbpublic.dto.AuthResponseDto;
import org.syantovich.wbpublic.errors.InvalidTokenException;
import org.syantovich.wbpublic.mappers.PersonMapper;
import org.syantovich.wbpublic.repository.AccessTokenRepository;
import org.syantovich.wbpublic.repository.PersonRepository;
import org.syantovich.wbpublic.repository.RefreshTokenRepository;
import org.syantovich.wbpublic.services.TokenService;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    final PersonRepository personRepository;
    final PersonMapper personMapper;
    final RefreshTokenRepository refreshTokenRepository;
    final AccessTokenRepository accessTokenRepository;

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private long expirationTime;
    @Value("${jwt.refresh-secret}")
    private String refreshSecret;
    @Value("${jwt.refresh-expiration}")
    private long refreshExpirationTime;
    private SecretKey secretKey;
    @Getter
    private SecretKey refreshSecretKey;

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        refreshSecretKey = Keys.hmacShaKeyFor(refreshSecret.getBytes());
    }

    public AccessToken createToken(UserDetails userDetails, RefreshToken refreshToken) {
        String username = userDetails.getUsername();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        var roles = authorities.stream().map(GrantedAuthority::getAuthority).toArray(String[]::new);
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);
        String token = Jwts.builder().setSubject(username).claim("roles", roles).setIssuedAt(now).setExpiration(expiryDate).signWith(secretKey, SignatureAlgorithm.HS256).compact();

        AccessToken accessToken = new AccessToken();
        accessToken.setToken(token);
        accessToken.setExpiryDate(expiryDate);
        accessToken.setRefreshToken(refreshToken);
        accessTokenRepository.save(accessToken);

        return accessToken;
    }

    @Override
    public RefreshToken createRefreshToken(UserDetails userDetails) {
        String username = userDetails.getUsername();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshExpirationTime);
        String refreshToken = Jwts.builder().setSubject(username).setIssuedAt(now).setExpiration(expiryDate).signWith(refreshSecretKey, SignatureAlgorithm.HS256).compact();

        RefreshToken tokenEntity = new RefreshToken();
        tokenEntity.setToken(refreshToken);
        tokenEntity.setExpiryDate(expiryDate);
        tokenEntity.setPerson(personRepository.findByEmail(username).orElseThrow(() -> new IllegalArgumentException("User with this email not found")));
        refreshTokenRepository.save(tokenEntity);

        return tokenEntity;
    }

    public Authentication parseToken(String token) {
        var claims = Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        var username = claims.getSubject();
        var roles = (List<String>) claims.get("roles");
        Collection<GrantedAuthority> authorities = roles.stream().map(role -> (GrantedAuthority) () -> role).toList();
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

    public void validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token).getPayload();
        } catch (Exception e) {
            throw new InvalidTokenException("Invalid access token");
        }
    }

    public void validateRefreshToken(String token) {
        try {
            var tokenEntity = refreshTokenRepository.findByToken(token).orElseThrow(() -> new InvalidTokenException("Invalid refresh token"));
            if (tokenEntity.getExpiryDate().before(new Date())) {
                refreshTokenRepository.deleteByToken(token);
                throw new InvalidTokenException("Refresh token expired");
            }
            Jwts.parser().setSigningKey(refreshSecretKey).build().parseClaimsJws(token).getPayload();
        } catch (Exception e) {
            throw new InvalidTokenException("Invalid refresh token");
        }
    }

    @Transactional
    public AuthResponseDto refresh(String refreshToken) {
        this.validateRefreshToken(refreshToken);
        refreshTokenRepository.deleteByToken(refreshToken);
        var claims = Jwts.parser().setSigningKey(this.getRefreshSecretKey()).build().parseClaimsJws(refreshToken).getBody();
        var username = claims.getSubject();
        var personEntity = personRepository.findByEmail(username).orElseThrow(() -> new IllegalArgumentException("User with this email not found"));
        var personDto = personMapper.toDto(personEntity);
        RefreshToken newRefreshToken = this.createRefreshToken(personEntity);
        AccessToken newToken = this.createToken(personEntity, newRefreshToken);
        return new AuthResponseDto(newToken.getToken(), newRefreshToken.getToken(), personDto);
    }

    public void invalidate(String accessToken) {
        try {
            this.validateToken(accessToken);
            AccessToken tokenEntity = accessTokenRepository.findByToken(accessToken);
            RefreshToken refreshToken = tokenEntity.getRefreshToken();
            accessTokenRepository.deleteByToken(accessToken);
            refreshTokenRepository.deleteByToken(refreshToken.getToken());
        } catch (Exception ignored) {
        }

    }
}
