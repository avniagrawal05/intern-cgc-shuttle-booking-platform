package com.hms.security.config;

import com.hms.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

/**
 * Security configuration for Spring Security with JWT
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers("/hms/api/v1/auth/**").permitAll()
                .requestMatchers("/hms/actuator/health").permitAll()
                .requestMatchers("/hms/swagger-ui/**", "/hms/api-docs/**", "/hms/swagger-ui.html").permitAll()
                
                // Read operations - authenticated users
                .requestMatchers(HttpMethod.GET, "/hms/api/v1/patients/**").hasAnyRole("ADMIN", "DOCTOR", "RECEPTIONIST")
                .requestMatchers(HttpMethod.GET, "/hms/api/v1/doctors/**").hasAnyRole("ADMIN", "DOCTOR", "RECEPTIONIST", "PATIENT")
                .requestMatchers(HttpMethod.GET, "/hms/api/v1/appointments/**").hasAnyRole("ADMIN", "DOCTOR", "RECEPTIONIST", "PATIENT")
                .requestMatchers(HttpMethod.GET, "/hms/api/v1/billings/**").hasAnyRole("ADMIN", "RECEPTIONIST", "PATIENT")
                
                // Write operations - admin and receptionist only
                .requestMatchers(HttpMethod.POST, "/hms/api/v1/patients/**").hasAnyRole("ADMIN", "RECEPTIONIST")
                .requestMatchers(HttpMethod.PUT, "/hms/api/v1/patients/**").hasAnyRole("ADMIN", "RECEPTIONIST")
                .requestMatchers(HttpMethod.DELETE, "/hms/api/v1/patients/**").hasRole("ADMIN")
                
                .requestMatchers(HttpMethod.POST, "/hms/api/v1/doctors/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/hms/api/v1/doctors/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/hms/api/v1/doctors/**").hasRole("ADMIN")
                
                .requestMatchers(HttpMethod.POST, "/hms/api/v1/appointments/**").hasAnyRole("ADMIN", "RECEPTIONIST", "PATIENT")
                .requestMatchers(HttpMethod.PUT, "/hms/api/v1/appointments/**").hasAnyRole("ADMIN", "RECEPTIONIST", "DOCTOR")
                .requestMatchers(HttpMethod.DELETE, "/hms/api/v1/appointments/**").hasAnyRole("ADMIN", "RECEPTIONIST")
                
                .requestMatchers(HttpMethod.POST, "/hms/api/v1/billings/**").hasAnyRole("ADMIN", "RECEPTIONIST")
                .requestMatchers(HttpMethod.PUT, "/hms/api/v1/billings/**").hasAnyRole("ADMIN", "RECEPTIONIST")
                .requestMatchers(HttpMethod.DELETE, "/hms/api/v1/billings/**").hasRole("ADMIN")
                
                // Actuator endpoints - admin only
                .requestMatchers("/hms/actuator/**").hasRole("ADMIN")
                
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
