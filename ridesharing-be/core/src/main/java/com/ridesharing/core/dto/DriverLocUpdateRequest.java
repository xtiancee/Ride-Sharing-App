package com.ridesharing.core.dto;

import com.ridesharing.core.model.ClientStatus;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class DriverLocUpdateRequest {
    private String driverId;
    private ClientStatus status;
}
