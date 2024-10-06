package com.alipour.learning.configs;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import javax.net.ssl.SSLException;

@Configuration
@EnableWebFlux
public class AppConfig {

//    @Bean
//    @Primary
//    public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
//        return new PropertySourcesPlaceholderConfigurer();
//    }

    @Bean
    public WebClient webClient() throws SSLException {
        SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();
        TcpClient tcpClient = TcpClient.create()
                .secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));
        HttpClient httpClient = HttpClient.from(tcpClient);
        return WebClient.builder()
                .filters(exchangeFilterFunctions -> {
//                    exchangeFilterFunctions.add(logRequest());
                })
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {

            StringBuilder sb = new StringBuilder("Request: \n");
            //append clientRequest method and url
            clientRequest
                    .headers()
                    .forEach((name, values) -> values.forEach(value -> sb.append(name).append("=").append(values).append("\n")));

            System.out.println(clientRequest.attributes());
            System.out.println(sb);

            return Mono.just(clientRequest);
        });
    }
}
