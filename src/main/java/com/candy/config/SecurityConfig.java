package com.candy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(auth -> auth
                        .antMatchers("/", "/custom-login", "/error").permitAll()  // Publicly accessible endpoints
                        .anyRequest().authenticated()                            // All other endpoints require authentication
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/custom-login")    // Specify custom login page
                        .defaultSuccessUrl("/")       // Redirect after successful login
                );

        return http.build();
    }
}
