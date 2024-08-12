package org.syantovich.wbpublic.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.syantovich.wbpublic.dto.SendVerifiedCodeDto;
import org.syantovich.wbpublic.dto.ValidateVerifiedCodeDto;
import org.syantovich.wbpublic.services.VerificationCodeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/verification")
public class VerificationController {
    private final VerificationCodeService verificationCodeService;

    @PostMapping("/send")
    public void sendVerificationCode(@RequestBody SendVerifiedCodeDto sendVerifiedCodeDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        verificationCodeService.sendCode(sendVerifiedCodeDto.getPersonId(), username);
    }

    @PostMapping("/validate")
    public void validateVerificationCode(@RequestBody ValidateVerifiedCodeDto validateVerifiedCodeDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        verificationCodeService.validateCode(username, validateVerifiedCodeDto.getCode());
    }

}

