package com.ridesharing.core.dto;

import com.ridesharing.core.model.RideStatus;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RideDto {

    private String id;
    private String riderName;
    private String driverName;
    private String riderId;
    private String driverId;
    private int fare;
    private String source;
    private String destination;
    private RideStatus status;
    private String date;
}
