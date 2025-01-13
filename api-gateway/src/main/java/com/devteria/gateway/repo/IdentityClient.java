package com.devteria.gateway.repo;

import com.devteria.gateway.dto.request.IntrospectRequest;
import com.devteria.gateway.dto.response.ApiResponse;
import com.devteria.gateway.dto.response.IntrospectResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

import java.awt.*;

public interface IdentityClient {
    @PostExchange(url = "/auth/introspect" , contentType = MediaType.APPLICATION_JSON_VALUE)
    Mono<ApiResponse<IntrospectResponse>> introspect(@RequestBody IntrospectRequest request);

}
