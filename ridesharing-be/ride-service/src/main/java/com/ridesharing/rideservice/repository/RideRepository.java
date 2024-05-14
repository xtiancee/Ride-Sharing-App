package com.ridesharing.rideservice.repository;

import com.ridesharing.core.model.RideStatus;
import com.ridesharing.rideservice.model.Ride;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface RideRepository extends CrudRepository<Ride, String>, QueryByExampleExecutor<Ride> {

    Ride findByDriverId(String driverId);
    Ride findByRiderId(String riderId);
    Ride findByDriverIdAndStatusOrStatus(String driverId, RideStatus status1, RideStatus status2);
    Ride findByRiderIdAndStatusOrStatus(String riderId, RideStatus status1, RideStatus status2);

    Ride findByDriverIdAndStatus(String driverId, RideStatus status);
    Ride findByRiderIdAndStatus(String riderId, RideStatus status);

}
