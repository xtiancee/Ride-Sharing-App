package com.ridersharing.notificationservice.client;

import com.ridesharing.core.dto.NewRideRequest;
import com.ridesharing.core.dto.RideDto;
import com.ridesharing.core.dto.RideRequestApprovalDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("ride-service")
public interface RideClient {

    @PostMapping("/api/v1/ride")
    RideDto initiateRide(NewRideRequest request);

    @PostMapping("/api/v1/ride/request-approval")
    void processRequestApproval(@RequestBody RideRequestApprovalDto request);
}
