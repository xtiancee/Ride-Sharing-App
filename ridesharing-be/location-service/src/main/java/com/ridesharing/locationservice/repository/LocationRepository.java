package com.ridesharing.locationservice.repository;

import com.ridesharing.core.model.ClientStatus;
import com.ridesharing.core.model.UserType;
import com.ridesharing.locationservice.model.UserLocation;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Point;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends CrudRepository<UserLocation, String>, QueryByExampleExecutor<UserLocation> {
    List<UserLocation> findByCoordinatesNear(Point point);
    Optional<UserLocation> findLocationById(String id);
    List<UserLocation> findByCoordinatesWithin(Circle circle);
    List<UserLocation> findByTypeAndCoordinatesWithin(UserType type, Circle circle);

    List<UserLocation> findByTypeAndStatusAndCoordinatesWithin(UserType type, ClientStatus status, Circle circle);
}
