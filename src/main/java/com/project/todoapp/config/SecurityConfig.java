package com.project.todoapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //@formatter:off
        return http.authorizeHttpRequests()
                .requestMatchers("/todolist/**").authenticated()
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .and()
                .oauth2Login()
                .and().build();
        //@formatter:on
    }

}
