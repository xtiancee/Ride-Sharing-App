package com.ridesharing.core.dto;

import com.ridesharing.core.model.ClientStatus;
import com.ridesharing.core.model.UserType;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserLocUpdateRequest {
    private String userTypeId;
    private String firstName;
    private String lastName;
    private String userId;
    private UserType userType;
    private ClientStatus status;
    private double lat;
    private double lng;
}
