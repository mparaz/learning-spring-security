package com.mparaz.lss;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

// This class must be a @Configuration to let the WebSecurityConfigurerAdapter methods run.
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class LearningWebSecurity extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // This is required for the roles to take effect.
        http
                .authorizeRequests()
                .anyRequest().authenticated();

        // Use JEE and map in the roles.
//        http.jee().mappableRoles("mockRole");

        // Subvert the Java EE-provided roles.
        http.jee().authenticatedUserDetailsService(new LearningAuthenticationUserDetailsService());
    }
}
