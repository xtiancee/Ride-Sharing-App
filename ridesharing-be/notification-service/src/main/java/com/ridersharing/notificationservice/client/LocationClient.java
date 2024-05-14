package com.ridersharing.notificationservice.client;

import com.ridesharing.core.dto.UserLocUpdateRequest;
import com.ridesharing.core.dto.UserLocationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("location-service")
public interface LocationClient {

    @PostMapping("/api/v1/location")
    UserLocUpdateRequest updateUserLocation(UserLocUpdateRequest request);

    @GetMapping("/api/v1/location/user-location/{id}")
    UserLocationDto requestUserLocation(@PathVariable("id") String id);
}
