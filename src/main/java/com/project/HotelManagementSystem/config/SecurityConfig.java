package com.project.HotelManagementSystem.config;

import com.project.HotelManagementSystem.security.jwt.AuthEntryPointJwt;
import com.project.HotelManagementSystem.security.jwt.AuthTokenFilter;
import com.project.HotelManagementSystem.service.AbstractService;
import com.project.HotelManagementSystem.service.CustomAuthenticationFailureHandler;
import com.project.HotelManagementSystem.service.CustomAuthenticationSuccessHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
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
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authTokenFilter(){
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(abstractService);
        auth.setPasswordEncoder(passwordEncoder);
        auth.setHideUserNotFoundExceptions(false);
        return auth;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain adminSiteSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/login", "", "/logout", "/register", "/registerUser",
                "/addresses/**","/amenities/**","/cities/**","/countries/**",
                "/authenticateTheUser",
                "/exports/**","/home/**","/hotels/**","/policies/**","/profiles/**",
                "/bookings/**","/hotelDetail/**","/search/**",
                "/promotions/**","/propertyDescriptions/**","/regions/**","/reviews/**",
                "/roles/**","/rooms/**", "/admins/**", "/editors/**", "/users/**", "/customers/**",
                "/select-role", "/set-active-role",
                "/static/assets/**", "/css/**",
                "/confirm-account/**", "/forget-password",
                "/confirm-otp", "/reset-password", "/error");

        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
        );

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/login","/register","/registerUser","/static/assets/**","/css/**","/confirm-account/**","/error", "/forget-password","/confirm-otp","/reset-password").permitAll()
                .requestMatchers("/admins/**").hasRole("ADMIN")
                .requestMatchers("/editors/**").hasAnyRole("EDITOR", "ADMIN")
                .requestMatchers("/users/**").hasAnyRole("EDITOR", "ADMIN")
                .requestMatchers("/customers/**").hasAnyRole("EDITOR", "ADMIN")
                .anyRequest().authenticated()
        );

        http.formLogin(form -> form
                .loginPage("/login?error=false")
                .loginProcessingUrl("/authenticateTheUser")
                .successHandler(customAuthenticationSuccessSuccessHandler)
                .failureHandler(customAuthenticationFailureHandler)
                .permitAll()
        );

        http.exceptionHandling(exception -> exception.accessDeniedPage("/access_denied"));

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.securityMatcher("/api/**");
        http.csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
//                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests.requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers(("/swagger-ui/**")).permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .anyRequest().authenticated());

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authTokenFilter(),
                UsernamePasswordAuthenticationFilter.class);

        http.headers(headers -> headers.frameOptions(
                HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        return http.build();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().requestMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**"));
    }

    @PostConstruct
    public void enableInheritableThreadLocal() {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

}
