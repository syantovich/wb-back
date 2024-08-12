package org.syantovich.wbpublic.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.syantovich.wbpublic.dto.AuthResponseDto;
import org.syantovich.wbpublic.dto.LoginDto;
import org.syantovich.wbpublic.dto.PersonDto;
import org.syantovich.wbpublic.dto.RegisterDto;

import java.util.List;

public interface PersonService extends UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    public List<PersonDto> getAllUsers();

    public PersonDto getPersonByEmail(String email);
}
