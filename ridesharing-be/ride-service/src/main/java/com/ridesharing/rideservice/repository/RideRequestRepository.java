package com.ridesharing.rideservice.repository;

import com.ridesharing.rideservice.model.RideRequest;
import org.springframework.data.repository.CrudRepository;

public interface RideRequestRepository extends CrudRepository<RideRequest, String> {
    RideRequest findByRideId(String id);
}
