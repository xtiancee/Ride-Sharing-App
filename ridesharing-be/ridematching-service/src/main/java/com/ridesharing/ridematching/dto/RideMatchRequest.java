package com.ridesharing.ridematching.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RideMatchRequest {
    private String rideId;
    private double lng;
    private double lat;
}
