package com.ridesharing.rideservice.service;

import com.ridesharing.core.dto.*;
import com.ridesharing.core.model.ClientStatus;
import com.ridesharing.core.model.RideRequestStatus;
import com.ridesharing.core.model.RideStatus;
import com.ridesharing.core.utils.Constants;
import com.ridesharing.rideservice.client.LocationClient;
import com.ridesharing.rideservice.client.RideMatchingClient;
import com.ridesharing.rideservice.dto.DriverRideRequestDto;
import com.ridesharing.rideservice.model.Ride;
import com.ridesharing.rideservice.model.RideRequest;
import com.ridesharing.rideservice.repository.RideRepository;
import com.ridesharing.rideservice.repository.RideRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Point;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import util.Utility;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RideService {


    private final RideRepository rideRepository;
    private final RideRequestRepository rideRequestRepository;
    private final KafkaTemplate<String, RiderRideApprovedDto> kafkaTemplateRideApproved;
    private final KafkaTemplate<String, com.ridesharing.core.dto.RideMatchRequest> kafkaTemplateRideMatch;
    private final KafkaTemplate<String, DriverRequest> kafkaTemplateDriverRequest;
    private final LocationClient locationRestClient;
    private final RideMatchingClient rideMatchingRestClient;

    public RideDto initiateRideAndMatch(NewRideRequest request){

        String[] source = request.getSource().split(",");
        String[] destination = request.getDestination().split(",");

        Point sourceLocation = new Point(Double.parseDouble(source[0]), Double.parseDouble(source[1]));
        Point destLocation = new Point(Double.parseDouble(destination[0]), Double.parseDouble(destination[1]));

        var ride = Ride.builder()
                .riderId(request.getRiderId())
                .riderName(request.getRiderName())
                .fare(request.getFare())
                .driverId("N/A")
                .driverName("N/A")
                .source(sourceLocation)
                .destination(destLocation)
                .status(RideStatus.FARE_ESTIMATED)
                .date(LocalDateTime.now())
                .build();

        ride = rideRepository.save(ride);

        log.info("Saving Ride and sending topic");

        log.info("Saved Ride is {} ", ride);

        // publish to matching service
        sendMatchRequest(kafkaTemplateRideMatch, ride);

        var deserializing = Utility.ConvertRideToDto(ride);

        log.info("Ride value {} ", deserializing);

        return deserializing;
    }

    public RideDto updateRide(Ride ride){
        var ryd = rideRepository.save(ride);
        return Utility.ConvertRideToDto(ryd);
    }

    public Ride findById(String id){
        return  rideRepository.findById(id).orElse(null);
    }

    public RideRequest saveDriverRideRequest(DriverRideRequestDto request){

        Optional<RideRequest> rideReq = rideRequestRepository.findById(request.getRideId());
        RideRequest req = null;

        // if request already exists, update

        if(rideReq.isPresent()){
            req = rideReq.get();
            req.setDriverId(request.getDriverId());
            req.setNextDriverId(request.getNextDriverId());
            req.setStatus(req.getStatus());

            return rideRequestRepository.save(req);

        }else{
            var reqR = RideRequest.builder()
                    .rideId(request.getRideId())
                    .driverId(request.getDriverId())
                    .nextDriverId(request.getNextDriverId())
                    .status(RideRequestStatus.PENDING)
                    .build();

            return rideRequestRepository.save(reqR);
        }
    }

    public void processApproval(RideRequestApprovalDto request){

        log.info("Ride process approval kicked {} ", request);

        if(request.isApproved()) {

            log.info("Approval type is approved ");

            // Update Ride Status
            Ride theRide = rideRepository.findById(request.getRideId()).orElse(null);
            assert theRide != null;
            theRide.setDriverId(request.getDriverId());
            theRide.setDriverName(request.getDriverName());
            theRide.setStatus(RideStatus.IN_RIDE);
            rideRepository.save(theRide);

            // Update RideRequest in DB
            RideRequest theRequest = rideRequestRepository.findByRideId(request.getRideId());
            theRequest.setStatus(RideRequestStatus.ACCEPTED);
            rideRequestRepository.save(theRequest);

            // update Driver Location Status to InRide
            DriverLocUpdateRequest updatedDriverLoc = DriverLocUpdateRequest.builder()
                    .driverId(request.getDriverId())
                    .status(ClientStatus.IN_RIDE)
                    .build();

            UserLocationDto driverLocation = locationRestClient.updateDriverLocAfterApproval(updatedDriverLoc).getBody();

            log.info("driverLocation In Ride Service: {} ", driverLocation);

            if(driverLocation != null){
                // Notify Rider
                var approvedRideNotify = RiderRideApprovedDto.builder()
                        .ride(Utility.ConvertRideToDto(theRide))
                        .driverName(driverLocation.getFirstName() + " " + driverLocation.getLastName())
                        .driverLat(driverLocation.getLat())
                        .driverLng(driverLocation.getLng())
                        .build();

                Message<RiderRideApprovedDto> msg = MessageBuilder
                        .withPayload(approvedRideNotify)
                        .setHeader(KafkaHeaders.TOPIC, Constants.RIDER_RIDE_APPROVED_TOPIC)
                        .build();

                kafkaTemplateRideApproved.send(msg);
            }else{
                log.info("Driver location not found during approval");
            }

        }else{

            log.info("Approval type is declined");

            // check for next driver, if null,
            Ride theRide = rideRepository.findById(request.getRideId()).orElse(null);
            RideRequest theRequest = rideRequestRepository.findByRideId(request.getRideId());

            if(theRequest.getNextDriverId().equals("N/A")){
                // call matching service
                // publish to matching service

                assert theRide != null;
                sendMatchRequest(kafkaTemplateRideMatch, theRide);

                theRequest.setDriverId("N/A");
                theRequest.setNextDriverId("N/A");
                rideRequestRepository.save(theRequest);
                return ;
            }

            // check if next driver is still in location and available

            log.info("NextDriver ID: {} ", theRequest.getNextDriverId());
            UserLocationDto driverLocation = locationRestClient.findDriverById(theRequest.getNextDriverId());

            log.info("returned NextDriver Location: {} ", driverLocation);

            // if No, call Matching or Driver is not in Ride
            if(driverLocation == null || (driverLocation.getStatus().equals(ClientStatus.IN_RIDE))){

                // Call Matching Service
                assert theRide != null;
                sendMatchRequest(kafkaTemplateRideMatch, theRide);

                // Update RideRequest
                theRequest.setDriverId("N/A");
                theRequest.setNextDriverId("N/A");
                rideRequestRepository.save(theRequest);
                return;
            }

            // check if next driver already has a request
            DriverRequestLockDto driverLock = rideMatchingRestClient.getDriverLock(theRequest.getNextDriverId());

            log.info("driverLock Status: {} ", driverLock);

            if(driverLock == null) {
                log.info("driver Does Not Have Request Status: {} ", driverLock);
                // Else Next Driver is Free, send Him Request
                assert theRide != null;

                DriverRequest req = DriverRequest.builder()
                        .ride(Utility.ConvertRideToDto(theRide))
                        .driverId(theRequest.getNextDriverId())
                        .build();

                log.info("driver Request for Next Driver: {} ", req);

                Message<DriverRequest> msg = MessageBuilder
                        .withPayload(req)
                        .setHeader(KafkaHeaders.TOPIC, Constants.DRIVER_REQUEST_TOPIC)
                        .build();

                // Notify Next Driver
                kafkaTemplateDriverRequest.send(msg);

                // update RideRequest
                theRequest.setDriverId(theRequest.getNextDriverId());
                theRequest.setNextDriverId("N/A");
                rideRequestRepository.save(theRequest);
            }else{

                log.info("driver have Request, calling Matching service: {} ", driverLock);
                // send call ride matching service
                sendMatchRequest(kafkaTemplateRideMatch, theRide);
            }
        }
    }

    private void sendMatchRequest(KafkaTemplate<String, RideMatchRequest> kafkaTemplate, Ride ride){

        var newMatchRequest = RideMatchRequest.builder()
                .ride(Utility.ConvertRideToDto(ride))
                .build();

        Message<RideMatchRequest> msg = MessageBuilder
                .withPayload(newMatchRequest)
                .setHeader(KafkaHeaders.TOPIC, Constants.RIDE_TOPIC)
                .build();

        kafkaTemplate.send(msg);
    }
}
