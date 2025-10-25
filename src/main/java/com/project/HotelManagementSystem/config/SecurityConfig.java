package com.project.HotelManagementSystem.config;

import com.project.HotelManagementSystem.service.AbstractService;
import com.project.HotelManagementSystem.service.CustomAuthenticationFailureHandler;
import com.project.HotelManagementSystem.service.CustomAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Autowired
    private final AbstractService abstractService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessSuccessHandler;
    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(abstractService);
        auth.setPasswordEncoder(passwordEncoder);
        auth.setHideUserNotFoundExceptions(false);
        return auth;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/register","/registerUser","/static/assets/**","/confirm-account/**","/error").permitAll()
                        .requestMatchers("/select-role", "/set-active-role").authenticated()
                        .requestMatchers("/admins/**").hasRole("ADMIN")
                        .requestMatchers("/editors/**").hasAnyRole("EDITOR", "ADMIN")
                        .requestMatchers("/users/**").hasAnyRole("EDITOR", "ADMIN")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/authenticateTheUser")
                        .successHandler(customAuthenticationSuccessSuccessHandler)
                        .failureHandler(customAuthenticationFailureHandler)
                        .permitAll())
                .exceptionHandling(exception -> exception.accessDeniedPage("/access_denied"));
        return http.build();
    }

}
