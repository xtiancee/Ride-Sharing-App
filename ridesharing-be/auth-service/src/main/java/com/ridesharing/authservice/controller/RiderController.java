package com.ridesharing.authservice.controller;

import com.ridesharing.authservice.service.RiderService;
import com.ridesharing.core.dto.RiderSignupRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/rider")
@RequiredArgsConstructor
@Slf4j
public class RiderController {

    private final RiderService riderService;

    @PostMapping
    public ResponseEntity<?> saveRider(@RequestBody RiderSignupRequest request){
        log.info("Auth Add Rider fired {} ", request);
        return ResponseEntity.ok(riderService.saveRider(request));
    }
}
