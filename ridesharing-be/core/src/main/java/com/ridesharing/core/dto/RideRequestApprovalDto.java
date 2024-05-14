package com.ridesharing.core.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RideRequestApprovalDto {
    private String rideId;
    private String driverId;
    private String driverName;
    private String driverLocation;
    private boolean approved;
}
