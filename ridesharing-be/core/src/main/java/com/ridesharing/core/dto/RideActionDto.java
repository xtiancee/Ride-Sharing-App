package com.ridesharing.core.dto;

import com.ridesharing.core.model.RideActionType;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class RideActionDto {
    private RideActionType type;
    private RideDto ride;
}
