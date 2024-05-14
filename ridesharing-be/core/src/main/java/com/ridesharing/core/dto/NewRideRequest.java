package com.ridesharing.core.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class NewRideRequest {
    private String riderId;
    private String riderName;
    private int fare;
    private String source;
    private String destination;
}
