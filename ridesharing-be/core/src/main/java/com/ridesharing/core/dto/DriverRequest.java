package com.ridesharing.core.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DriverRequest {
    private String driverId;
    private RideDto ride;
}
