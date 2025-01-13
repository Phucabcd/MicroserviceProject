package com.devteria.gateway.configuration;

import com.devteria.gateway.dto.response.ApiResponse;
import com.devteria.gateway.service.IdentityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationFilter implements GlobalFilter, Ordered {
    IdentityService identityService;
    ObjectMapper objectMapper;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Enter authentication filter...");
        // B1: Get token from authorization header
        List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        //neu khong co header se khong dc truy cap
        if (CollectionUtils.isEmpty(authHeader))
            return unauthenticated(exchange.getResponse());

        String token = authHeader.getFirst().replace("Bearer ", "");
        log.info("token: {}", token);

        // B2: verify token
        // Deleagte identity service

        // B3: kiem tra introspect
        return identityService.introspect(token).flatMap(introspectResponseApiResponse -> {
            if (introspectResponseApiResponse.getResult().isValid())
                return  chain.filter(exchange);
            else
                return unauthenticated(exchange.getResponse());
        }).onErrorResume(throwable -> unauthenticated(exchange.getResponse()));

    }

    @Override
    public int getOrder() {
        return -1;
    }

    Mono<Void> unauthenticated(ServerHttpResponse response){
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(1444)
                .message("Unauthenticated")
                .build();

        String body = null;
        try {
             body = objectMapper.writeValueAsString(apiResponse);
        }catch (Exception e){
            e.printStackTrace();
        }

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return response.writeWith(
                Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }
}
