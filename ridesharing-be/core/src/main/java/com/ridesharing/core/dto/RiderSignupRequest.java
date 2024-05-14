package com.ridesharing.core.dto;

import lombok.Data;

@Data
public class RiderSignupRequest {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
