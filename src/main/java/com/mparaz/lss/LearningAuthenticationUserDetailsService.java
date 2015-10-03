package com.mparaz.lss;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class LearningAuthenticationUserDetailsService implements AuthenticationUserDetailsService {
    @Override
    public UserDetails loadUserDetails(Authentication token) throws UsernameNotFoundException {
        final String principal = (String) token.getPrincipal();

        // When using Java EE, the token will be pre-populated with the user (principal), but not the roles.

        // Look up the user from an API that will return the list of roles.
        final RestTemplate restTemplate = new RestTemplate();

        try {
            final ResponseEntity<String[]> rolesEntity = restTemplate.getForEntity("http://localhost:8080/r/{principal}",
                    String[].class, principal);

            final List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
            for (final String role: rolesEntity.getBody()) {
                authorities.add(new SimpleGrantedAuthority(role));
            }

            // Password does not matter because it was already preauthenticated by Java EE.
            return new User(principal, "mockPassword", authorities);

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new UsernameNotFoundException(principal + " not found");
            } else {
                // Just throw as usual.
                throw e;
            }
        }
    }
}
