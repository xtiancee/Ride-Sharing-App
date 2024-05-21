package com.ridesharing.rideservice.service;

import com.ridesharing.core.dto.ClientDisconnectDto;
import com.ridesharing.core.dto.ClientWhoDisconnectDto;
import com.ridesharing.core.dto.UserDto;
import com.ridesharing.core.model.RideStatus;
import com.ridesharing.core.model.UserType;
import com.ridesharing.core.utils.Constants;
import com.ridesharing.rideservice.client.LocationClient;
import com.ridesharing.rideservice.client.UserClient;
import com.ridesharing.rideservice.model.Ride;
import com.ridesharing.rideservice.repository.RideRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListenerService {

    private final LocationClient locationRestClient;
    private final UserClient userClient;
    private final RideRepository repository;

    private final KafkaTemplate<String, ClientWhoDisconnectDto> clientWhoDisconnectKafkaTemplate;

    @KafkaListener(topics = Constants.CLIENT_DISCONNECTS_TOPIC, containerFactory = "clientDisconnectedListenerContainerFactory")
    public void listenToClientDisconnect(ClientDisconnectDto request) {

        log.info("Client with ID {} disconnected ", request.getClientId());

        Ride ride = null;

        // Ride status to check for
        List<RideStatus> statusList = List.of(
                RideStatus.IN_RIDE,
                RideStatus.FARE_ESTIMATED
        );

        // delete Location
        this.locationRestClient.deleteLocation(request.getClientId());

        // Get User
        UserDto user = userClient.getUser(request.getClientId()).getBody();

        log.info("User Data: {} ", user);

        log.info("All Rides: {} ", repository.findAll());

        // find Ride and Invalidate
        assert user != null;

        if(user.getType().equals(UserType.DRIVER)){

            ride = repository.findByDriverIdAndStatus(request.getClientId(), statusList.get(0));

            if(ride == null){
                ride = repository.findByDriverIdAndStatus(request.getClientId(), statusList.get(1));
            }

            log.info("Ride Data: {} ", ride);

            if(ride != null) {
                ride.setStatus(RideStatus.CANCELLED);
                repository.save(ride);

                // Notify Rider driver disconnected
                var newClientWhoDisconnectReq = ClientWhoDisconnectDto.builder()
                        .destination("/rider")
                        .clientToNotify("RIDER")
                        .clientId(ride.getRiderId())
                        .build();

                Message<ClientWhoDisconnectDto> msg = MessageBuilder
                        .withPayload(newClientWhoDisconnectReq)
                        .setHeader(KafkaHeaders.TOPIC, Constants.CLIENT_WHO_DISCONNECTED_TOPIC)
                        .build();

                log.info("Kafka message sent for client who disconnected!");

                clientWhoDisconnectKafkaTemplate.send(msg);
            }


        }else if(user.getType().equals(UserType.RIDER)){

            ride = repository.findByRiderIdAndStatus(request.getClientId(), statusList.get(0));

            if(ride == null){
                ride = repository.findByRiderIdAndStatus(request.getClientId(), statusList.get(0));
            }

            log.info("Ride Data: {} ", ride);

            if(ride != null){
                ride.setStatus(RideStatus.CANCELLED);
                repository.save(ride);

                // Notify driver, Rider disconnected
                var newClientWhoDisconnectReq = ClientWhoDisconnectDto.builder()
                        .destination("/driver")
                        .clientToNotify("DRIVER")
                        .clientId(ride.getDriverId())
                        .build();

                Message<ClientWhoDisconnectDto> msg = MessageBuilder
                        .withPayload(newClientWhoDisconnectReq)
                        .setHeader(KafkaHeaders.TOPIC, Constants.CLIENT_WHO_DISCONNECTED_TOPIC)
                        .build();

                clientWhoDisconnectKafkaTemplate.send(msg);
            }

        }
    }
}
