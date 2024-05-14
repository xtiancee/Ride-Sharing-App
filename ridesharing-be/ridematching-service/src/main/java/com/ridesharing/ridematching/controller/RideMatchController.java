package com.ridesharing.ridematching.controller;

import com.ridesharing.core.dto.DriverRequestLockDto;
import com.ridesharing.core.dto.RideMatchRequest;
import com.ridesharing.ridematching.service.DriverRequestLockService;
import com.ridesharing.ridematching.service.RideMatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ride-matching")
@RequiredArgsConstructor
public class RideMatchController {

    private final RideMatchingService matchingService;
    private final DriverRequestLockService requestLockService;

    // Asynchronous
    @PostMapping
    public ResponseEntity<String> matchRiderToDriver(RideMatchRequest request){
        matchingService.matchRider(request);
        return ResponseEntity.ok("Matching Process began!");
    }

    @PatchMapping("/unlock-driver/{id}")
    public ResponseEntity<String> unlockDriver(@PathVariable("id") String id){
        requestLockService.unlockDriver(id);
        return ResponseEntity.ok("Driver Unlocked!");
    }

    @GetMapping("/driver-lock/{id}")
    public ResponseEntity<DriverRequestLockDto> getDriverLock(@PathVariable("id") String id){
        requestLockService.unlockDriver(id);
        return ResponseEntity.ok(requestLockService.getLockForDriver(id));
    }
}
