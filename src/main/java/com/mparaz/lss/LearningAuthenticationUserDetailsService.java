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

        // When using Java EE, the token will already be pre-populated with the user and roles from Java EE.

        // Look up the user

        final List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_mockRole"));

        // Password does not matter because it was already preauthenticated by Java EE.
        return new User(principal, "mockPassword", authorities);
    }
}
