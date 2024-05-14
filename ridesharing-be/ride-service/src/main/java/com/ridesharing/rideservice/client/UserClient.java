package com.ridesharing.rideservice.client;

import com.ridesharing.core.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "auth-service")
public interface UserClient {

    @GetMapping(value = "/api/v1/auth/users/{email}")
    ResponseEntity<UserDto> getUser(@PathVariable("email") String email);
}
