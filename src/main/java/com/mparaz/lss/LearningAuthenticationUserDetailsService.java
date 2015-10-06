package com.mparaz.lss;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class LearningAuthenticationUserDetailsService implements AuthenticationUserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LearningAuthenticationUserDetailsService.class);

    @Autowired
    private RestTemplate restTemplate;

    @PostConstruct
    public void init() {
        LOGGER.debug("loadUserDetails: restTemplate={}, this={}", restTemplate, this);
    }


    @Override
    public UserDetails loadUserDetails(Authentication token) throws UsernameNotFoundException {

        final String principal = (String) token.getPrincipal();

        // When using Java EE, the token will be pre-populated with the user (principal), but not the roles.

        try {
            // restTemplate could be null because...
            LOGGER.debug("loadUserDetails: restTemplate={}, this={}", restTemplate, this);

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
