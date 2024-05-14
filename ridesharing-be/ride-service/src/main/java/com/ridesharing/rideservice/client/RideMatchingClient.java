package com.ridesharing.rideservice.client;

import com.ridesharing.core.dto.DriverRequestLockDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("ride-matching-service")
public interface RideMatchingClient {

    @GetMapping("/api/v1/ride-matching/driver-lock/{id}")
    DriverRequestLockDto getDriverLock(@PathVariable("id") String driverId);
}
