package com.ridesharing.rideservice.model;

import com.ridesharing.core.model.RideStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.GeoIndexed;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
//@RedisHash(value="ride", timeToLive = 60L)
@RedisHash(value="ride")
@Builder
public class Ride implements Serializable {

    @Id
    private String id;
    private String riderName;
    private String driverName;
    private String riderId;
    private String driverId;
    private int fare;

    @GeoIndexed
    private Point source;

    @GeoIndexed
    private Point destination;
    private RideStatus status;

    private LocalDateTime date;
}
