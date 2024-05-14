package com.ridesharing.ridematching.client;

import com.ridesharing.core.dto.DriverRideRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("ride-service")
public interface RideClient {

    @PostMapping("/api/v1/ride/driver-request")
    DriverRideRequestDto saveRideRequest(DriverRideRequestDto request);
}
