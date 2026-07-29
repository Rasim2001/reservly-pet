package com.reservly.apigateway.filter;

import com.reservly.common.SecurityHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class UserIdPropagationFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return exchange.getPrincipal()
                .cast(JwtAuthenticationToken.class)
                .map(JwtAuthenticationToken::getToken)
                .map(jwt -> withUserHeaders(exchange, jwt.getSubject(), jwt.getClaimAsString("role")))
                .defaultIfEmpty(stripUserHeaders(exchange))
                .flatMap(chain::filter);
    }


    @Override
    public int getOrder() {
        return -1;
    }


    private ServerWebExchange withUserHeaders(ServerWebExchange exchange, String userId, String role) {
        return exchange.mutate()
                .request(r -> r.headers(
                        headers -> {
                            headers.set(SecurityHeaders.USER_ID, userId);
                            headers.set(SecurityHeaders.USER_ROLE, role);
                        }))
                .build();
    }

    private ServerWebExchange stripUserHeaders(ServerWebExchange exchange) {
        return exchange.mutate()
                .request(r -> r
                        .headers(headers -> {
                            headers.remove(SecurityHeaders.USER_ID);
                            headers.remove(SecurityHeaders.USER_ROLE);
                        }))
                .build();
    }
}