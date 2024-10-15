package com.alipour.learning.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MyReactiveAuthorizationManager implements ReactiveAuthorizationManager<ServerWebExchange> {

    private final WebClient webClient;
    private final List<ServerWebExchangeMatcher> ignoredAuthorizationRequests;

    private MyReactiveAuthorizationManager(WebClient webClient, List<ServerWebExchangeMatcher> ignoredAuthorizationRequests) {
        this.webClient = webClient;
        this.ignoredAuthorizationRequests = ignoredAuthorizationRequests;
    }

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, ServerWebExchange exchange) {
        return Flux.fromIterable(ignoredAuthorizationRequests)
                .flatMap(matcher -> matcher.matches(exchange))
                .collectList()
                .filter(matchResults -> matchResults.stream().noneMatch(ServerWebExchangeMatcher.MatchResult::isMatch))
                .flatMap(ignored -> authorize(authentication, exchange))
                .switchIfEmpty(Mono.fromSupplier(() -> new AuthorizationDecision(true)));
    }

    private Mono<AuthorizationDecision> authorize(Mono<Authentication> authentication, ServerWebExchange exchange) {
        return authentication
                .filter(Authentication::isAuthenticated)
                .flatMap(token -> evaluatePolicy(exchange))
                .filter(KeycloakAuthorizationResponse::isResult)
                .map(policyEvaluationResponse -> new AuthorizationDecision(true))
                .switchIfEmpty(Mono.fromSupplier(() -> new AuthorizationDecision(false)));
    }


    private Mono<KeycloakAuthorizationResponse> evaluatePolicy(ServerWebExchange exchange) {
        final ServerHttpRequest request = exchange.getRequest();
        final MultiValueMap<String, String> map = this.buildRequestBody(request);
        final BodyInserters.FormInserter<String> data = BodyInserters.fromFormData(map);

        final String first = request.getHeaders().toSingleValueMap().get(HttpHeaders.AUTHORIZATION);

        return webClient.post()
                .uri("http://127.0.0.1:9002/realms/zabix-realm/protocol/openid-connect/token")
                .header("Authorization", first)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(data)
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().is2xxSuccessful()) {
                        return clientResponse.bodyToMono(KeycloakAuthorizationResponse.class);
                    } else if (clientResponse.statusCode().equals(HttpStatus.UNAUTHORIZED)) {
                        return clientResponse.bodyToMono(JsonNode.class)
                                .doOnNext(p -> System.out.println("##### Status: " + clientResponse.statusCode() + " Response:" + p))
                                .map(ignored -> new KeycloakAuthorizationResponse(false));
                    } else if (clientResponse.statusCode().equals(HttpStatus.FORBIDDEN)) {
                        return clientResponse.bodyToMono(JsonNode.class)
                                .doOnNext(p -> System.out.println("##### Status: " + clientResponse.statusCode() + " Response:" + p))
                                .map(ignored -> new KeycloakAuthorizationResponse(false));
                    } else {
                        return Mono.just(new KeycloakAuthorizationResponse(false));
                    }
                })
                .doOnNext(System.out::println);
    }

    final ObjectMapper mapper = new ObjectMapper();

    private MultiValueMap<String, String> buildRequestBody(ServerHttpRequest request) {
        final HttpMethod methodCall = request.getMethod();
        final RequestPath path = request.getPath();
        Map<String, List<String>> claims = new HashMap<>();
        claims.put("uri_claim", List.of(request.getPath().value()));
        claims.put("request_method_claim", List.of(request.getMethod().name()));

//        body.mapNotNull(p -> {
//                    try {
//                        return mapper.readValue(p.asInputStream(), JsonNode.class);
//                    } catch (IOException e) {
//                        return null;
//                    }
//                })
//                .collectList()
//                .subscribe(System.out::println);

        String encodedBytes;
        try {
            encodedBytes = Base64.getEncoder().encodeToString(mapper.writeValueAsBytes(claims));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "urn:ietf:params:oauth:grant-type:uma-ticket");
        map.add("audience", "zabix-client");
        map.add("response_mode", "decision");
        map.add("permission_resource_format", "uri");
        map.add("permission_resource_matching_uri", "true");
        map.add("permission", path.value() + "#" + methodCall.name());
        map.add("claim_token_format", "urn:ietf:params:oauth:token-type:jwt");
        map.add("claim_token", encodedBytes);
        return map;
    }

}
