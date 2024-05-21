package com.ridesharing.rideservice.repository;

import com.ridesharing.core.model.RideStatus;
import com.ridesharing.rideservice.model.Ride;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface RideRepository extends CrudRepository<Ride, String>, QueryByExampleExecutor<Ride> {
    Ride findByDriverIdAndStatus(String driverId, RideStatus status);
    Ride findByRiderIdAndStatus(String riderId, RideStatus status);
}
