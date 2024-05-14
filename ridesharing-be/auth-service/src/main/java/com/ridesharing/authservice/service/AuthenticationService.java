package com.ridesharing.authservice.service;

import com.ridesharing.authservice.config.security.jwt.JwtService;
import com.ridesharing.authservice.dto.AuthRequest;
import com.ridesharing.authservice.dto.AuthResponse;
import com.ridesharing.authservice.model.User;
import com.ridesharing.core.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Transactional
    public ResponseEntity<?> authenticateUser(AuthRequest request){

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword()));

        var user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("user not found with email: " + request.getEmail()));

        var jwtToken = jwtService.generateToken(user);

        return ResponseEntity.ok(AuthResponse.builder()
                        .access_token(jwtToken)
                        .user(user)
                        .build());
    }

    public List<UserDto> getUsers(){
        return userService.findAll()
                 .stream()
                 .map(user -> {
                     return UserDto.builder()
                             .id(user.getId())
                             .firstName(user.getFirstName())
                             .lastName(user.getLastName())
                             .email(user.getEmail())
                             .type(user.getType())
                             .build();
                 }).collect(Collectors.toList());
    }

    public UserDto getUser(String email){

        Optional<User> userByEmail = userService.findByEmail(email);

        if(userByEmail.isPresent()){
            User user = userByEmail.get();
            return UserDto.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .type(user.getType())
                    .build();
        }else{
            return null;
        }
    }
}
