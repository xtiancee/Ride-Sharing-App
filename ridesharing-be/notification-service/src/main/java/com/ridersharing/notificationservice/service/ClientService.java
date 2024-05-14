package com.ridersharing.notificationservice.service;

import com.ridersharing.notificationservice.client.LocationClient;
import com.ridersharing.notificationservice.client.RideClient;
import com.ridesharing.core.dto.*;
import com.ridesharing.core.model.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {

    private final LocationClient locationRestClient;
    private final RideClient rideClient;
    private final SimpMessageSendingOperations messageTemplate;

    public void updateLocation(UserLocUpdateRequest request){
        locationRestClient.updateUserLocation(request);
    }

    public void onNewRide(NewRideRequest request) {

        var ride = rideClient.initiateRide(request);

        NotificationMsg msg = NotificationMsg.builder()
                .type(NotificationType.RIDE_INITIATED)
                .message(ride)
                .build();

        log.info("New Ride Initiated {} ", ride);
        messageTemplate.convertAndSendToUser(request.getRiderId(), "/rider", msg);
    }

    public void onRideApproval(RideRequestApprovalDto request){
        this.rideClient.processRequestApproval(request);
    }

    public void requestUserLocation(RequestUserLocDto request){

        UserLocationDto location = this.locationRestClient.requestUserLocation(request.getUserToLocate());

        NotificationType notificationType;
        String destination;

        if(request.getFromType().equals("DRIVER")){
            notificationType = NotificationType.RIDER_LOCATION;
            destination = "/driver";
        }else{
            notificationType = NotificationType.DRIVER_LOCATION;
            destination = "/rider";
        }

        NotificationMsg msg = NotificationMsg.builder()
                .type(notificationType)
                .message(location)
                .build();

        log.info("User Location Retrieved {} ", location);
        messageTemplate.convertAndSendToUser(request.getFrom(), destination, msg);
    }
}
