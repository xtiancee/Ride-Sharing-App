package com.ridesharing.authservice.service;

import com.ridesharing.authservice.model.Role;
import com.ridesharing.authservice.model.User;
import com.ridesharing.core.dto.DriverSignupRequest;
import com.ridesharing.core.model.UserType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverService {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<?> saveDriver(DriverSignupRequest request){

        var userExist = userService.findByEmail(request.getEmail().toLowerCase());

        if(userExist.isPresent())
            return ResponseEntity.badRequest().body("Email not available choose another!");

        Role role = roleService.findByName("DRIVER").get();

        User newUser = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail().toLowerCase())
                .type(UserType.DRIVER)
                .car(request.getCarType())
                .carPlate(request.getCarNumberPlate())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(role))
                .build();

        var savedUser = userService.save(newUser);
        return ResponseEntity.ok(savedUser);
    }
}
