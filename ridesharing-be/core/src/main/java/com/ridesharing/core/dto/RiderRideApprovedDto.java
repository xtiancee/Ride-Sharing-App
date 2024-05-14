package com.ridesharing.core.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class RiderRideApprovedDto {
    private RideDto ride;
    private String driverName;
    private double driverLat;
    private double driverLng;
}
