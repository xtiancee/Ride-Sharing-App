package com.ridersharing.notificationservice.service;

import com.ridesharing.core.dto.ClientWhoDisconnectDto;
import com.ridesharing.core.dto.DriverRequest;
import com.ridesharing.core.dto.NotificationMsg;
import com.ridesharing.core.dto.RiderRideApprovedDto;
import com.ridesharing.core.model.NotificationType;
import com.ridesharing.core.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final SimpMessageSendingOperations messageTemplate;

    @KafkaListener(
            topics = Constants.DRIVER_REQUEST_TOPIC,
            groupId = Constants.DRIVER_REQUEST_TOPIC_GROUP,
            containerFactory = "driverRequestListenerContainerFactory")
    public void listenForDriverRequest(DriverRequest msg) {

        log.info("Listened to driver request {} ", msg);

        // send notification to driver
        NotificationMsg message = NotificationMsg.builder()
                .type(NotificationType.REQUEST_PENDING)
                .message(msg)
                .build();

        messageTemplate.convertAndSendToUser(msg.getDriverId(), "/driver", message);
    }

    @KafkaListener(
            topics = Constants.RIDER_RIDE_APPROVED_TOPIC,
            groupId = Constants.RIDE_APPROVED_GROUP,
    containerFactory = "rideApprovedListenerContainerFactory")
    public void listenForDriverRideApproved(RiderRideApprovedDto msg){

        // notify rider for ride approved
        NotificationMsg message = NotificationMsg.builder()
                .type(NotificationType.DRIVER_APPROVED)
                .message(msg)
                .build();

        messageTemplate.convertAndSendToUser(msg.getRide().getRiderId(), "/rider", message);
    }

    @KafkaListener(
            topics = Constants.CLIENT_WHO_DISCONNECTED_TOPIC,
            groupId = Constants.CLIENT_WHO_DISCONNECTED_TOPIC_GROUP,
            containerFactory = "clientWhoDisconnectedListenerContainerFactory")
    public void listenForClientWhoDisconnected(ClientWhoDisconnectDto msg){

        NotificationType notificationType;
        String whoDisconnected = "";

        if(msg.getClientToNotify().equals("DRIVER")){
            notificationType = NotificationType.CLIENT_DISCONNECTED;
            whoDisconnected = "Rider Disconnected";
        }else{
            notificationType = NotificationType.DRIVER_DISCONNECTED;
            whoDisconnected = "Driver Disconnected";
        }

        // notify rider for ride approved
        NotificationMsg message = NotificationMsg.builder()
                .type(notificationType)
                .message("Opps! " + whoDisconnected)
                .build();

        messageTemplate.convertAndSendToUser(msg.getClientId(), msg.getDestination(), message);
    }
}
