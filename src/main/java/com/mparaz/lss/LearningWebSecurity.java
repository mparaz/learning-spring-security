package com.mparaz.lss;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

// This class must be a @Configuration to let the WebSecurityConfigurerAdapter methods run.
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class LearningWebSecurity extends WebSecurityConfigurerAdapter {

    // LearningAuthenticationUserDetailsService is now autowired instead of new()
    // since it's a @Component that pulls in other dependencies.
    @Autowired
    private LearningAuthenticationUserDetailsService learningAuthenticationUserDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // This is required for the roles to take effect.
        http
                .authorizeRequests()
                .anyRequest().authenticated();

        // Use the Java EE role in Spring Security.
        // Note, no ROLE_ prefix here
        http.jee().mappableAuthorities("ROLE_javaeeRole");

        // Alternative option
//        http.jee().mappableRoles("javaeeRole");
    }
}
