package com.alipour.learning.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtGrantedAuthoritiesConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.AuthorizationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final RestAuthenticationEntryPoint authenticationEntryPoint;

    public SecurityConfig(RestAuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange(exchange -> exchange.pathMatchers(HttpMethod.OPTIONS, "/**").permitAll());
        http.authorizeExchange(exchange -> exchange.pathMatchers(HttpMethod.GET, WHITE_LIST_APIS).permitAll());
        http.authorizeExchange(exchange -> exchange.anyExchange().authenticated());

        http.oauth2ResourceServer(oauth2ResourceServer -> {
            oauth2ResourceServer.authenticationEntryPoint(authenticationEntryPoint)
                    .jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(authenticationConverter()));
        });
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
        http.cors(ServerHttpSecurity.CorsSpec::disable);

        return http.build();
    }

    @Bean
    public List<ServerWebExchangeMatcher> ignoredAuthorizationRequests() {
        List<ServerWebExchangeMatcher> list = new ArrayList<>();
        list.add(ServerWebExchangeMatchers.pathMatchers(HttpMethod.OPTIONS, "/**"));
        list.add(ServerWebExchangeMatchers.pathMatchers(HttpMethod.GET, WHITE_LIST_APIS));
        return list;
    }

    private final String[] WHITE_LIST_APIS = new String[]{
            "/webjars/**",
            "/swagger**",
            "/api-documents/**",
            "/actuator/**",
            "/restaurant/public/**"
    };

    @Bean
    public AuthorizationWebFilter authorizationWebFilter(MyReactiveAuthorizationManager authorizationManager) {
        return new AuthorizationWebFilter(authorizationManager);
    }


    @Bean
    public DefaultMethodSecurityExpressionHandler expressionHandler() {
        DefaultMethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler =
                new DefaultMethodSecurityExpressionHandler();
        defaultMethodSecurityExpressionHandler.setDefaultRolePrefix("");
        return defaultMethodSecurityExpressionHandler;
    }

    @Bean
    public ReactiveJwtAuthenticationConverter authenticationConverter() {
        ReactiveJwtAuthenticationConverter c = new ReactiveJwtAuthenticationConverter();
        JwtGrantedAuthoritiesConverter cv = new JwtGrantedAuthoritiesConverter();
        cv.setAuthorityPrefix(""); // Default "SCOPE_"
        cv.setAuthoritiesClaimName("roles"); // Default "scope" or "scp"
        ReactiveJwtGrantedAuthoritiesConverterAdapter converterAdapter = new ReactiveJwtGrantedAuthoritiesConverterAdapter(cv);
        c.setJwtGrantedAuthoritiesConverter(converterAdapter);
        return c;
    }

}
