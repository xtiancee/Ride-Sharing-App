package com.ridesharing.rideservice.dto;

import com.ridesharing.core.model.RideRequestStatus;
import lombok.Data;

import java.io.Serializable;

@Data
public class DriverRideRequestDto implements Serializable {
    private String id;
    private String rideId;
    private String driverId;
    private String nextDriverId;
    private RideRequestStatus status;
}
