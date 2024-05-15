package com.ridesharing.locationservice.controller;

import com.ridesharing.core.dto.DriverLocUpdateRequest;
import com.ridesharing.core.dto.UserLocUpdateRequest;
import com.ridesharing.core.dto.UserLocationDto;
import com.ridesharing.locationservice.model.UserLocation;
import com.ridesharing.locationservice.service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Point;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/location")
@RequiredArgsConstructor
@Slf4j
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    public ResponseEntity<UserLocation> updateLocation(@RequestBody UserLocUpdateRequest request)
    {
        log.info("update Location called with {} ", request);
        return ResponseEntity.ok(locationService.saveLocation(request));
    }

    @PostMapping("/list")
    public ResponseEntity<List<UserLocation>> updateUsers(@RequestBody List<UserLocUpdateRequest> request)
    {
        return ResponseEntity.ok(locationService.saveLocations(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserLocation> getUserLocation(@PathVariable("id") String id)
    {
        return ResponseEntity.ok(locationService.findByUser(id));
    }

    @GetMapping("/driver/{id}")
    public ResponseEntity<UserLocationDto> getDriverLocation(@PathVariable("id") String id)
    {
        return ResponseEntity.ok(locationService.findDriverLocation(id));
    }

    @PostMapping("/drivers")
    public ResponseEntity<List<UserLocation>> getDriversWithinLocation(@RequestBody Point location) {
        var closeDrivers = locationService.getDriversForRider(location);
        log.info("returned close drivers {} ", closeDrivers);
        return ResponseEntity.ok(locationService.getDriversForRider(location));
    }

    @PostMapping(("/update-driver"))
    public ResponseEntity<UserLocationDto> updateDriverLocAfterApproval(@RequestBody DriverLocUpdateRequest request)
    {
        return ResponseEntity.ok(locationService.updateDriverLocAfterApproval(request));
    }

    @GetMapping("/user-location/{id}")
    public ResponseEntity<UserLocationDto> requestUserLoc(@PathVariable("id") String id)
    {
        return ResponseEntity.ok(locationService.requestUserLoc(id));
    }

    @DeleteMapping("/{id}")
    public void deleteUserLocation(@PathVariable("id") String id)
    {
        log.info("deleting location with ID: {}", id);
        locationService.deleteLocation(id);
    }
}
