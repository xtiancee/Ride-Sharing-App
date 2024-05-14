package com.ridesharing.authservice.client;

import com.ridesharing.authservice.dto.RiderDto;
import com.ridesharing.core.dto.RiderSignupRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("rider-service")
public interface RiderClient {
    @PostMapping("/api/v1/rider")
    ResponseEntity<RiderDto> addRider(RiderSignupRequest request);
}
