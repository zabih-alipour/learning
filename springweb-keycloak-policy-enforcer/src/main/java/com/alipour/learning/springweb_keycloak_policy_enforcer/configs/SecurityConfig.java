package com.alipour.learning.springweb_keycloak_policy_enforcer.configs;

import org.keycloak.adapters.authorization.integration.jakarta.ServletPolicyEnforcerFilter;
import org.keycloak.adapters.authorization.spi.ConfigurationResolver;
import org.keycloak.adapters.authorization.spi.HttpRequest;
import org.keycloak.representations.adapters.config.PolicyEnforcerConfig;
import org.keycloak.util.JsonSerialization;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain webSecurity(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize.requestMatchers(HttpMethod.GET, "/courses", "/courses/*").permitAll());
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());
        http.oauth2ResourceServer(oauthServer ->
                oauthServer.jwt(jwtConfigurer -> jwtConfigurer
                                .jwkSetUri("http://localhost:9002/realms/keycloak-policy-enforcer-realm/protocol/openid-connect/certs")

//                                .authenticationManager(new AuthenticationManager() {
//                                    @Override
//                                    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//                                        return authentication;
//                                    }
//                                })
//                                .jwtAuthenticationConverter(new Converter<Jwt, AbstractAuthenticationToken>() {
//                                    @Override
//                                    public AbstractAuthenticationToken convert(Jwt source) {
//                                        return new JwtAuthenticationToken(source);
//                                    }
//                                })
                ));
        http.addFilterAfter(createPolicyEnforcerFilter(), BearerTokenAuthenticationFilter.class);
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(AbstractHttpConfigurer::disable);
        return http.build();
    }

    private ServletPolicyEnforcerFilter createPolicyEnforcerFilter() {
        PolicyEnforcerConfig config;

        try {
            config = JsonSerialization.readValue(getClass().getResourceAsStream("/policy-enforcer.json"), PolicyEnforcerConfig.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ServletPolicyEnforcerFilter(request -> config);
    }
}
