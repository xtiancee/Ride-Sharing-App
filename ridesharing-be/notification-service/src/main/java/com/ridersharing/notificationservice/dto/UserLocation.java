package com.ridersharing.notificationservice.dto;


import com.ridesharing.core.model.ClientStatus;
import com.ridesharing.core.model.UserType;
import lombok.Data;

import java.awt.*;
import java.io.Serializable;

@Data
public class UserLocation implements Serializable {
    private String id;
    private String firstName;
    private String lastName;
    private String userId;
    private UserType type;
    private ClientStatus status;
    private Point coordinates;
}
