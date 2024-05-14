package com.ridesharing.authservice.client;

import com.ridesharing.authservice.dto.DriverDto;
import com.ridesharing.core.dto.DriverSignupRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("driver-service")
public interface DriverClient {
    @PostMapping("/api/v1/driver")
    ResponseEntity<DriverDto> addDriver(DriverSignupRequest request);
}
