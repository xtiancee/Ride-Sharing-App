package com.ridesharing.rideservice.client;

import com.ridesharing.core.dto.DriverLocUpdateRequest;
import com.ridesharing.core.dto.UserLocationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "location-service")
public interface LocationClient {

    @PostMapping("/api/v1/location/update-driver")
    ResponseEntity<UserLocationDto> updateDriverLocAfterApproval(@RequestBody DriverLocUpdateRequest request);

    @GetMapping("/api/v1/location/driver/{id}")
    UserLocationDto findDriverById(@PathVariable("id") String id);

    @DeleteMapping("/api/v1/location/{id}")
    void deleteLocation(@PathVariable("id") String id);
}
