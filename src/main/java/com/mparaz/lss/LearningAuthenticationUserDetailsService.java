package com.mparaz.lss;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.List;

public class LearningAuthenticationUserDetailsService implements AuthenticationUserDetailsService {
    @Override
    public UserDetails loadUserDetails(Authentication token) throws UsernameNotFoundException {
        final String principal = (String) token.getPrincipal();

        // Look up the user
        final List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("mockRole"));

        return new User(principal, "mockPassword", authorities);
    }
}
