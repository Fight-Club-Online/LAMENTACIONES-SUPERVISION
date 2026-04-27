package com.lamentaciones.supervision.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SupervisionSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            // IMPORTANTE: Eliminamos la política STATELESS para que la sesión básica funcione en el navegador
            .authorizeHttpRequests(auth -> auth
                // Documentación libre
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/webjars/**").permitAll()
                
                // Endpoints públicos
                .requestMatchers(HttpMethod.GET,  "/api/v1/supervision/status/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/supervision/report").permitAll()
                .requestMatchers(HttpMethod.GET,  "/api/v1/supervision/chat/**").permitAll()
                .requestMatchers(HttpMethod.GET,  "/api/v1/supervision/notifications/**").permitAll()
                .requestMatchers(HttpMethod.PATCH,"/api/v1/supervision/notifications/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()

                // Todo lo que sea ADMIN requerirá el password de Docker
                .requestMatchers("/api/v1/admin/**").authenticated()
                .anyRequest().authenticated()
            )
            // ESTA ES LA CLAVE: Activa el diálogo de usuario/password en el navegador y Swagger
            .httpBasic(Customizer.withDefaults());
            
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173", "https://lamentaciones-frontend.vercel.app"));
        config.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}