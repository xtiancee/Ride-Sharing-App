package com.ridesharing.apigw.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {
    public static final List<String> openApiEndPoints = List.of(
            "/api/v1/auth/**",
            "/api/v1/subscriptions/**",
            "/api/v1/test",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
    );

    public Predicate<ServerHttpRequest> isSecured =
             request -> openApiEndPoints
                     .stream()
                     .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
