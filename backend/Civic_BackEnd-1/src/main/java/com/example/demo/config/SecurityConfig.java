package com.example.demo.config;




import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF to allow POST requests from Postman
            .csrf(csrf -> csrf.disable())
            // Require authentication for all requests
            .authorizeHttpRequests(auth -> auth
                .anyRequest().authenticated()
            )
            // Enable Basic Authentication (what you are using in Postman)
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}

