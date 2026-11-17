package com.casino.slot.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Desactivamos CSRF (porque es una API REST, no un formulario HTML)
                .csrf(csrf -> csrf.disable())

                // 2. No usamos sesiones (cada petición se autentica con el token)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. Configuramos las reglas de las rutas
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas (no requieren token)
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/admin/simulate").permitAll() // Dejamos el simulador libre para probar
                        .requestMatchers("/api/games/**").permitAll() // Los juegos son públicos para que cualquiera vea la config

                        // Cualquier otra ruta requiere autenticación
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}