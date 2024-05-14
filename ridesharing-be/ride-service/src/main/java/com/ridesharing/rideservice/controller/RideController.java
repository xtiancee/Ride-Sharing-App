package com.ridesharing.rideservice.controller;

import com.ridesharing.core.dto.RideRequestApprovalDto;
import com.ridesharing.rideservice.dto.DriverRideRequestDto;
import com.ridesharing.core.dto.NewRideRequest;
import com.ridesharing.core.dto.RideDto;
import com.ridesharing.rideservice.model.Ride;
import com.ridesharing.rideservice.model.RideRequest;
import com.ridesharing.rideservice.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ride")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;

    @PostMapping
    public RideDto initiateRideAndMatch(@RequestBody NewRideRequest request){
        return rideService.initiateRideAndMatch(request);
    }

    @PutMapping
    public ResponseEntity<RideDto> updateRide(@RequestBody Ride ride){
        return ResponseEntity.ok(rideService.updateRide(ride));
    }

    @PostMapping("/driver-request")
    public ResponseEntity<RideRequest> saveDriverRequest(@RequestBody DriverRideRequestDto request)
    {
        return ResponseEntity.ok(rideService.saveDriverRideRequest(request));
    }

    @PostMapping("/request-approval")
    public ResponseEntity<Void> processRequestApproval(@RequestBody RideRequestApprovalDto request)
    {
        rideService.processApproval(request);
        return ResponseEntity.ok().build();
    }
}
