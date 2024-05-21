package com.ridersharing.notificationservice.controller;

import com.ridersharing.notificationservice.service.ClientService;
import com.ridesharing.core.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@RequiredArgsConstructor
public class NotificationController {

    private final ClientService clientService;

    @MessageMapping("/location.update")
    public void updateLocation(@Payload UserLocUpdateRequest request) {
        clientService.updateLocation(request);
    }

    @MessageMapping("/ride.new")
    public void newRide(@Payload NewRideRequest request) {
        log.info("Received newRideRequest {} ", request);
        clientService.onNewRide(request);
    }

    @MessageMapping("/ride.approving")
    public void processRideApproval(@Payload RideRequestApprovalDto request) {
        log.info("Received, processing Ride Approval {} ", request);
        clientService.onRideApproval(request);
    }

    @MessageMapping("/ride.requestUserLocation")
    public void requestUserLocation(@Payload RequestUserLocDto request) {
        log.info("Received, requestUserLocation {} ", request);
        clientService.requestUserLocation(request);
    }

    @MessageMapping("/ride.startRide")
    public void startRide(@Payload RideActionRequestDto request) {
        log.info("Received, startRide Request {} ", request);
        clientService.startRide(request);
    }

    @MessageMapping("/ride.endRide")
    public void endRide(@Payload RideActionRequestDto request) {
        log.info("Received, endRide Request {} ", request);
        clientService.endRide(request);
    }
}

