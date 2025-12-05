package com.escooter.apigateway.config;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.config.HttpClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;

@Slf4j
@Configuration
public class HttpClientConfig {

    @Bean
    public HttpClientCustomizer httpClientCustomizer() {
        return httpClient -> {
            try {
                SslContext sslContext = SslContextBuilder
                        .forClient()
                        .trustManager(InsecureTrustManagerFactory.INSTANCE)
                        .build();

                return httpClient.secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));
            } catch (SSLException e) {
                log.error("Failed to configure SSL context", e);
                return httpClient;
            }
        };
    }
}
