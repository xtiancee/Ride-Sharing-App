package com.ridesharing.authservice.dto;

import lombok.Data;

@Data
public class RiderDto {
    private String id;
    private String userId;
    private String firstName;
    private String lastName;
}
