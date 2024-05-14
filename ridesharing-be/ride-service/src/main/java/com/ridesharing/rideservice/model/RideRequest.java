package com.ridesharing.rideservice.model;

import com.ridesharing.core.model.RideRequestStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@RedisHash(value="ride_requests", timeToLive = 60L)
@Builder
public class RideRequest implements Serializable {

    @Id
    private String id;

    @Indexed
    private String rideId;

    @Indexed
    private String driverId;

    private String nextDriverId;

    private RideRequestStatus status;
}
