package edu.sia.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                // Autorizaciones
                .authorizeExchange(auth -> auth
                        .anyExchange().authenticated()
                )
                // Habilitar OAuth2 Resource Server con JWT
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(reactiveJwtAuthenticationConverter()))
                )
                // Desactivar CSRF (para API REST)
                .csrf(ServerHttpSecurity.CsrfSpec::disable);

        return http.build();
    }


    /**
     * Configures the reactive JWT authentication converter to extract roles from custom claims.
     * @return configured ReactiveJwtAuthenticationConverterAdapter
     */
    @Bean
    public ReactiveJwtAuthenticationConverterAdapter reactiveJwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            String role = jwt.getClaimAsString("role");
            if (role == null || role.isEmpty()) {
                return List.of();
            }
            String authority = "ROLE_" + role;
            return List.of(new SimpleGrantedAuthority(authority));
        });
        return new ReactiveJwtAuthenticationConverterAdapter(converter);
    }

    /**
     * Configures the method security expression handler with a custom permission evaluator.
     * Uses @Primary to override Spring's default bean.
     * @param permissionEvaluator custom permission evaluator
     * @return configured MethodSecurityExpressionHandler
     */
    @Bean
    @Primary
    MethodSecurityExpressionHandler methodSecurityExpressionHandler(ReactivePermissionEvaluator permissionEvaluator) {
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        handler.setPermissionEvaluator(permissionEvaluator);
        return handler;
    }
}
