package com.ridesharing.authservice.controller;

import com.ridesharing.authservice.dto.AuthRequest;
import com.ridesharing.core.dto.UserDto;
import com.ridesharing.authservice.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authService;

    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest request){
        log.info("Authentication Controller called at: {}", LocalDateTime.now());
        return authService.authenticateUser(request);
    }

    @GetMapping(value = "/users")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        log.info("Authentication Controller called for users : {}", LocalDateTime.now());
        return ResponseEntity.ok(authService.getUsers());
    }

    @GetMapping(value = "/users/{email}")
    public ResponseEntity<UserDto> getUser(@PathVariable("email") String email){
        log.info("Authentication Controller called for user : {}", email);
        return ResponseEntity.ok(authService.getUser(email));
    }
}
