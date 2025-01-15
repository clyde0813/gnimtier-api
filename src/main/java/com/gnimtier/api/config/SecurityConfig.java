package com.gnimtier.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

//Deprecated method가 너무 많음. 자료조사 후 다시 짜야함
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //csrf disable
        http.csrf(AbstractHttpConfigurer::disable);
        //formLogin disable
        http.csrf(AbstractHttpConfigurer::disable);
        //http basic Authentication disable
        http.httpBasic(AbstractHttpConfigurer::disable);
        //authorizeHttpRequests config
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/")
                .permitAll()
                .anyRequest()
                .authenticated()
        );
        //session stateless
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        return http.build();
    }
}
