package com.ridesharing.core.dto;

import com.ridesharing.core.model.ClientStatus;
import com.ridesharing.core.model.UserType;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class UserLocationDto {

    private String id;
    private String firstName;
    private String lastName;
    private String userId;
    private UserType type;
    private ClientStatus status;
    private double lng;
    private double lat;
}
