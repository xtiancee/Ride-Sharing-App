package com.ridesharing.locationservice.model;

import com.ridesharing.core.model.ClientStatus;
import com.ridesharing.core.model.UserType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.GeoIndexed;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@RedisHash(value="location", timeToLive = 60L)
@Builder
public class UserLocation implements Serializable {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String userId;

    @Indexed
    private UserType type;

    @Indexed
    private ClientStatus status;

    @GeoIndexed
    private Point coordinates;
}
