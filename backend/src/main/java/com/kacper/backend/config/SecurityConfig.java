package com.kacper.backend.config;

import com.kacper.backend.jwt.JWTAuthFilter;
import com.kacper.backend.user.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration for security
 *
 * @Author Kacper Karabinowski
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig
{
    private final CustomUserDetailsService customUserDetailsService;
    private final JWTAuthFilter jwtAuthFilter;

    /**
     * Security configuration for USER and ADMIN roles with METHOD permissions
     *
     * @param httpSecurity security configuration
     * @return SecurityFilterChain with security configuration
     * @throws Exception if something goes wrong
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity
    ) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(
                        request -> request.requestMatchers(
                                "/auth/login",
                                "/v2/api-docs",
                                "/v3/api-docs",
                                "/v3/v3/api-docs/**",
                                "/swagger-resources",
                                "/swagger-resources/**",
                                "/configuration/ui",
                                "/configuration/security",
                                "/swagger-ui/**",
                                "/webjars/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        .requestMatchers(HttpMethod.GET,
                                "/api/v1/user/"
                        ).hasAnyAuthority("ROLE_ADMIN")

                        .requestMatchers(HttpMethod.POST,
                                "/public/send/",
                                "/auth/register",
                                "/api/v1/sensor/{sensorId}",
                                "/api/v1/measurement/{sensorId}",
                                "/api/v1/device/"
                        ).hasAnyAuthority("ROLE_ADMIN")

                        .requestMatchers(HttpMethod.DELETE,
                                "/api/v1/sensor/{sensorId}",
                                "/api/v1/measurement/{measurementId}",
                                "/api/v1/device/{deviceId}",
                                "/api/v1/user/{id}"
                        ).hasAnyAuthority("ROLE_ADMIN")

                        .requestMatchers(HttpMethod.GET,
                                "/api/v1/sensor/{deviceId}",
                                "/api/v1/device/",
                                "/api/v1/device/{deviceId}",
                                "/api/v1/device/user/",
                                "/api/v1/device/admin/test",
                                "/api/v1/device/sensor/{deviceId}",
                                "/api/v1/device/measurement/{sensorId}",
                                "/api/v1/notifications/{numPage}",
                                "/api/v1/anomaly/{sensorId}",
                                "/api/v1/measurement/graph/{sensorId}"
                        ).hasAnyAuthority( "ROLE_USER", "ROLE_ADMIN")

                        .requestMatchers("/ws/**").permitAll()

                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(
                        jwtAuthFilter, UsernamePasswordAuthenticationFilter.class
                );
        return httpSecurity.build();
    }

    /**
     * @return authentication provider with custom user details service
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    /**
     * @return password encoder to encode passwords
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * @param authenticationConfiguration configuration for authentication
     * @return authentication manager
     * @throws Exception if something goes wrong
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
