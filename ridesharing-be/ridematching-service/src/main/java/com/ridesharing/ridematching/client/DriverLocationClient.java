package com.ridesharing.ridematching.client;

import com.ridesharing.ridematching.dto.CloseDriver;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.geo.Point;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("location-service")
public interface DriverLocationClient {

    @PostMapping("/api/v1/location/drivers")
    List<CloseDriver> getCloseDrivers(@RequestBody Point location);
}
