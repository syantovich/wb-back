package org.syantovich.wbpublic.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.syantovich.wbpublic.dto.PersonDto;
import org.syantovich.wbpublic.services.impl.PersonServiceImpl;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/persons")
@RequiredArgsConstructor
public class AllUsersController {
    private final PersonServiceImpl personService;

    @GetMapping("/all")
    public List<PersonDto> getAllUsers() {
        return personService.getAllUsers();
    }

    @GetMapping("/me")
    public PersonDto getMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return personService.getPersonByEmail(authentication.getName());
    }

}
