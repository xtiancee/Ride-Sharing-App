package com.ridesharing.core.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RideMatchRequest {
    private RideDto ride;
}
