package com.mparaz.lss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

// extends SpringBootServletInitializer required for WAR files.
@SpringBootApplication
public class LearnSpringSecurityApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(LearnSpringSecurityApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(LearnSpringSecurityApplication.class, args);
    }
}
