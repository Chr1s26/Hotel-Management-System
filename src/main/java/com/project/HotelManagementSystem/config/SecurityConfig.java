package com.project.HotelManagementSystem.config;

import com.project.HotelManagementSystem.security.jwt.AuthEntryPointJwt;
import com.project.HotelManagementSystem.security.jwt.AuthTokenFilter;
import com.project.HotelManagementSystem.service.AbstractService;
import com.project.HotelManagementSystem.service.CustomAuthenticationFailureHandler;
import com.project.HotelManagementSystem.service.CustomAuthenticationSuccessHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

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
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
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
        http.securityMatcher("/login", "/logout", "/register", "/registerUser","/authenticateTheUser",
                "/addresses/**","/amenities/**","/cities/**","/countries/**","/reindex",
                "/exports/**","/home/**","/hotels/**","/policies/**","/profiles/**",
                "/bookings/**","/hotelDetail/**",
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
//                .requestMatchers( "/bookings/**", "/hotelDetail/**", "/search/**","/api/v1/search/autocomplete").permitAll()
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
                .successHandler(customAuthenticationSuccessHandler)
                .failureHandler(customAuthenticationFailureHandler)
                .permitAll()
        );
        //UsernamePasswordAuthenticationFilter extract name and email => authentication manager.authenticate (we write authentication Manger in above)=> authentication manager calls daoauthenticationprovider => provider calls load by user name
        http.exceptionHandling(exception -> exception.accessDeniedPage("/access_denied"));

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.securityMatcher("/api/**");
        http.csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests.requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/v1/auth/**", "/api/v1/public/**").permitAll()
                        .requestMatchers("api/v1/stripe/webhook").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
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

    @Value("${app.cors.allowed-origins:http://localhost:5173,http://localhost:3000}")
    private String[] allowedOrigins;

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList(allowedOrigins));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(java.util.List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**",config);
        return source;
    }

}
