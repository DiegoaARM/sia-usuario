package edu.sia.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Autorizaciones
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
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
        String role = jwt.getClaimAsString("role");
        System.out.println("Role extraído del JWT: " + role);

        if (role == null || role.isEmpty()) {
            System.out.println("No se encontró rol en el JWT");
            return List.of();
        }

        String authority = "ROLE_" + role;
        System.out.println("Authority asignado: " + authority);
        return List.of(new SimpleGrantedAuthority(authority));
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

    /**
     * Configures the method security expression handler with a custom permission evaluator.
     * @param permissionEvaluator custom permission evaluator
     * @return configured MethodSecurityExpressionHandler
     */
    @Bean
    static MethodSecurityExpressionHandler methodSecurityExpressionHandler(CustomPermissionEvaluator permissionEvaluator) {
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        handler.setPermissionEvaluator(permissionEvaluator);
        return handler;
    }
}

