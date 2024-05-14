package com.ridesharing.ridematching.dto;

import lombok.Data;
import org.springframework.data.geo.Point;

@Data
public class CloseDriver {
    private String id;
    private String driverFirstName;
    private String driverLastName;
    private String driverUserId;
    private Point coordinates;
}
