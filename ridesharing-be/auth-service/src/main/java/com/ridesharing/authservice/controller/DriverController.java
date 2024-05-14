package com.ridesharing.authservice.controller;

import com.ridesharing.authservice.service.DriverService;
import com.ridesharing.core.dto.DriverSignupRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/driver")
@RequiredArgsConstructor
@Slf4j
public class DriverController {

    private final DriverService driverService;

    @PostMapping
    public ResponseEntity<?> saveDriver(@RequestBody DriverSignupRequest request){
        log.info("Auth Add Driver fired {} ", request);
        return ResponseEntity.ok(driverService.saveDriver(request));
    }
}
