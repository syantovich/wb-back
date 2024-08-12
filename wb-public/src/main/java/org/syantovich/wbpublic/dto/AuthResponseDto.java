package org.syantovich.wbpublic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDto {
    private String token;
    private String refreshToken;
    private PersonDto person;
}
