package com.ridesharing.locationservice.service;

import com.ridesharing.core.dto.DriverLocUpdateRequest;
import com.ridesharing.core.dto.UserLocUpdateRequest;
import com.ridesharing.core.dto.UserLocationDto;
import com.ridesharing.core.model.ClientStatus;
import com.ridesharing.core.model.UserType;
import com.ridesharing.locationservice.model.UserLocation;
import com.ridesharing.locationservice.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationService {

    private final LocationRepository repository;


    public UserLocation saveLocation(UserLocUpdateRequest request) {

        Optional<UserLocation> userCurrentLoc = repository.findLocationById(request.getUserTypeId());

        if(userCurrentLoc.isPresent()){
            UserLocation dbLoc = userCurrentLoc.get();
            dbLoc.setCoordinates(new Point(request.getLng(), request.getLat()));
            var locSaved = repository.save(dbLoc);
            return locSaved;
        }else{
            var location = UserLocation.builder()
                    .id(request.getUserTypeId())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .status(ClientStatus.ONLINE)
                    .userId(request.getUserId())
                    .type(request.getUserType())
                    .coordinates(new Point(request.getLng(), request.getLat()))
                    .build();
            var locSaved =  repository.save(location);
            return locSaved;
        }
    }

    public List<UserLocation> saveLocations(List<UserLocUpdateRequest> requests) {

        List<UserLocation> locations = new ArrayList<>();

        for(UserLocUpdateRequest request: requests){
            var location = UserLocation.builder()
                    .id(request.getUserTypeId())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .userId(request.getUserId())
                    .type(request.getUserType())
                    .coordinates(new Point(request.getLng(), request.getLat()))
                    .build();
            locations.add(location);
        }

        repository.saveAll(locations);
        return locations;
    }

    public UserLocation findByUser(String userId){
        return repository.findLocationById(userId).orElse(null);
    }

    public UserLocationDto requestUserLoc(String userId){

        var userLoc = repository.findLocationById(userId);

        if(userLoc.isPresent()){
            UserLocation loc = userLoc.get();
            return UserLocationDto.builder()
                    .id(loc.getId())
                    .firstName(loc.getFirstName())
                    .lastName(loc.getLastName())
                    .userId(loc.getUserId())
                    .type(loc.getType())
                    .status(loc.getStatus())
                    .lng(loc.getCoordinates().getX())
                    .lat(loc.getCoordinates().getY())
                    .build();
        }

        return null;
    }

    public List<UserLocation> getDriversForRider(Point location){

        Circle circle = new Circle(location,
                new Distance(1, RedisGeoCommands.DistanceUnit.KILOMETERS));

         return repository.findByTypeAndStatusAndCoordinatesWithin(UserType.DRIVER, ClientStatus.ONLINE, circle)
                 .stream()
                 .filter(l -> !l.getType().equals(UserType.RIDER))
                 .collect(Collectors.toList());
    }

    public UserLocationDto updateDriverLocAfterApproval(DriverLocUpdateRequest request) {

        Optional<UserLocation> userCurrentLoc = repository.findLocationById(request.getDriverId());

        if(userCurrentLoc.isPresent()){

            log.info("updateDriverLocation before update: {} ", userCurrentLoc.get());
            UserLocation dbLoc = userCurrentLoc.get();
            dbLoc.setStatus(ClientStatus.IN_RIDE);
            repository.save(dbLoc);
            log.info("updateDriverLocation after update: {} ", dbLoc);

            log.info("");

            return UserLocationDto.builder()
                    .id(dbLoc.getId())
                    .firstName(dbLoc.getFirstName())
                    .lastName(dbLoc.getLastName())
                    .userId(dbLoc.getUserId())
                    .type(dbLoc.getType())
                    .status(dbLoc.getStatus())
                    .lat(dbLoc.getCoordinates().getY())
                    .lng(dbLoc.getCoordinates().getX())
                    .build();
        }

        return null;
    }

    public UserLocationDto findDriverLocation(String id) {

        log.info("finding driver location: ");

        Optional<UserLocation> userCurrentLoc = repository.findLocationById(id);

        if(userCurrentLoc.isPresent()){

            UserLocation dbLoc = userCurrentLoc.get();

            log.info("found driver location: {} ", dbLoc);

            return UserLocationDto.builder()
                    .id(dbLoc.getId())
                    .firstName(dbLoc.getFirstName())
                    .lastName(dbLoc.getLastName())
                    .userId(dbLoc.getUserId())
                    .type(dbLoc.getType())
                    .status(dbLoc.getStatus())
                    .lat(dbLoc.getCoordinates().getY())
                    .lng(dbLoc.getCoordinates().getX())
                    .build();
        }

        return null;
    }

    public void deleteLocation(String id) {
        repository.deleteById(id);
    }
}
