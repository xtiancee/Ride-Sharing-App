package com.ridesharing.core.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DriverSignupRequest {
    private String userId;
    private String firstName;
    private String password;
    private String email;
    private String lastName;
    private String carType;
    private String carNumberPlate;
}
