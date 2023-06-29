package com.example.zuulservice.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {
    public LoggingFilter() {
        super(LoggingFilter.Config.class);
    }
    @Override
    public GatewayFilter apply(Config config) {
        GatewayFilter filter = new OrderedGatewayFilter((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("LoggingFilter filter basemessage: {} ", config.getBaseMessage());

            if (config.isPreLogger()) {
                log.info("LoggingFilter pre filter : request id -> {}", config.getBaseMessage());
            }

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                if (config.isPostLogger()) {
                    log.info("LoggingFilter post filter: response code -> {}", config.getBaseMessage());
                }
            }));
        }, Ordered.HIGHEST_PRECEDENCE);

        return filter;
}

    @Data
    public static class Config {
        private String baseMessage;
        private boolean isPreLogger;
        private boolean isPostLogger;
    }
}
