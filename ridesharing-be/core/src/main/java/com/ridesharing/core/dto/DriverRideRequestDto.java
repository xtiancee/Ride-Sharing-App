package com.ridesharing.core.dto;

import com.ridesharing.core.model.RideRequestStatus;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class DriverRideRequestDto implements Serializable {
    private String id;
    private String rideId;
    private String driverId;
    private String nextDriverId;
    private RideRequestStatus status;
}
