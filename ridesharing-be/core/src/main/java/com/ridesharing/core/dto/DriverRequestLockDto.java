package com.ridesharing.core.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class DriverRequestLockDto {
    private String id;
    private String driverId;
}
