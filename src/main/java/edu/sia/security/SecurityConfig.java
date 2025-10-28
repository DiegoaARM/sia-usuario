package edu.sia.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Autorizaciones
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/public/**").permitAll()  // endpoints públicos
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll()
                )
                // Habilitar OAuth2 Resource Server con JWT (nuevo formato)
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
                // Desactivar CSRF (solo si usas API REST)
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    /**
     * Extracts authorities from JWT claims.
     * Maps the "custom:role" claim to a Spring Security authority.
     * @param jwt JWT token
     * @return collection of granted authorities
     */
    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        Optional<String> role = jwt.getClaimAsString("role").describeConstable();
        if (role.isEmpty()) return List.of();
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.get()));
    }

    /**
     * Configures the JWT authentication converter to extract roles from custom claims.
     * @return configured JwtAuthenticationConverter
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(this::extractAuthorities);
        return converter;
    }
}

