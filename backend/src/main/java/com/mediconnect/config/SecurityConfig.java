package com.mediconnect.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Public search and read endpoints for hospitals and doctors
                .requestMatchers(HttpMethod.GET, "/api/v1/hospitals/**", "/api/v1/doctors/**").permitAll()
                // Management of hospitals and doctors restricted to ADMIN
                .requestMatchers(HttpMethod.POST, "/api/v1/hospitals/**", "/api/v1/doctors/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/hospitals/**", "/api/v1/doctors/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/v1/hospitals/**", "/api/v1/doctors/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/hospitals/**", "/api/v1/doctors/**").hasRole("ADMIN")
                // All other endpoints
                .anyRequest().permitAll()
            )
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
