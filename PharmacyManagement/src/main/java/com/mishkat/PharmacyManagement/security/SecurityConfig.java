package com.mishkat.PharmacyManagement.security;

import com.mishkat.PharmacyManagement.authentication.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity          // Enables @PreAuthorize on controllers
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(auth -> auth

                        // ── Public endpoints (no token needed) ────────────
                        // Temporarily permitting all endpoints for dev testing based on requirement
                        .requestMatchers("/api/auth/**", "/**").permitAll()


                ).authenticationProvider(customAuthenticationProvider()).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();


    }


    @Bean
    public AuthenticationProvider customAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);


        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // configuration.setAllowedOrigins(List.of("http://localhost:4200", "http://192.168.88.250:4200"));
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "DELETE", "PUT", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(false); // Must be false if allowed origins contains wildcard "*"

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}


//Login,email verification and security code created roadmap
//step by step,remember every file is connected
//previous file correction
//1.UserRole (Enum) * Defines all application access levels, including the newly added CUSTOMER role.
//2.User (Entity) * Implements Spring Security's UserDetails to link your database table directly with the authentication system.
//3.UserRepository (Interface) * Provides standard database queries to look up users by their username or email.
//new file
//4.CustomUserDetailsService (Service Class) * Loads user details during login and ensures the account status is active (enabled == true).
//5.JwtUtil (Class) * Responsible for generating secure JWT tokens upon successful login and parsing/validating them later.
//6.JwtAuthFilter (Class) * Intercepts every incoming API request to extract and verify the bearer token before allowing access.
//7.LoginRequestDTO (Class) * Binds incoming JSON payloads containing the user's login credentials (usernameOrEmail, password).
//8.LoginResponseDTO (Class) * Structures the outgoing response containing the JWT token, profile data, and assigned pharmacy branch details.
//9.AuthService (Class) * Contains core business logic that invokes the AuthenticationManager and packages data into the LoginResponseDTO.
//10.AuthController (RestController Class) * Exposes the actual /api/auth/login HTTP endpoint for frontend integration.
//11.SecurityConfig (Class) * The main security gateway where password encoding, CORS, and endpoint rules (temporarily permitAll()) are configured.
//12.EmailService
//13.ForgotPasswordRequestDTO
//14.ResetPasswordRequestDTO