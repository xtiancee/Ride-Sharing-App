package com.ridesharing.authservice.dto;

import com.ridesharing.core.model.ClientStatus;
import lombok.Data;

@Data
public class DriverDto {

    private String id;
    private String userId;
    private String firstName;
    private String lastName;
    private String carType;
    private String carNumberPlate;
    private ClientStatus driverStatus;
}
