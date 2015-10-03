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
                .anyRequest().authenticated()
                .and()
                .httpBasic();

        http.jee().authenticatedUserDetailsService(new LearningAuthenticationUserDetailsService());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("inmemory").password("inmemory").roles("mockRole");
    }
}
