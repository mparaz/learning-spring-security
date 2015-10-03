package com.mparaz.lss;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;

// This class must be a @Configuration to let the WebSecurityConfigurerAdapter methods run.
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class LearningWebSecurity extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // This is required for the roles to take effect.
        // /goOpen and /goClosed will not require authentication.
        http
                .authorizeRequests()
                .antMatchers("/goOpen", "/goClosed").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // The user name, password, and password can be given here as a starting point.
        // roles() prepends the ROLE_ in the String.
        auth.inMemoryAuthentication().withUser("inmemory").password("inmemory").roles("mockRole");

        // Additional configurations stack up and don't replace the previous one.
        auth.inMemoryAuthentication().withUser("inmemory2").password("inmemory2").roles("mockRole");

        // This can be seen as a longer way of writing out the lines above.
        auth.userDetailsService(new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                // Load the user details from somewhere, anywhere.
                // Spring Security will do the password matching.
                if ("customuser".equals(username)) {
                    return new User("customuser", "customuser",
                            Arrays.asList(new SimpleGrantedAuthority("ROLE_mockRoleCustom")));
                } else {
                    throw new UsernameNotFoundException(username + "not here");
                }
            }
        });
    }
}
