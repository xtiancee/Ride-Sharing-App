package com.ridesharing.ridematching.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@RedisHash(value="ride", timeToLive = 60L)
@Builder
public class DriverRequestLock implements Serializable {

    @Id
    private String id;
    private String driverId;
}
