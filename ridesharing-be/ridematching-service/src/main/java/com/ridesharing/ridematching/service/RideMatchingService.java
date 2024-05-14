package com.ridesharing.ridematching.service;

import com.ridesharing.core.dto.DriverRequest;
import com.ridesharing.core.dto.DriverRideRequestDto;
import com.ridesharing.core.dto.RideMatchRequest;
import com.ridesharing.core.model.RideRequestStatus;
import com.ridesharing.core.utils.Constants;
import com.ridesharing.ridematching.client.DriverLocationClient;
import com.ridesharing.ridematching.client.RideClient;
import com.ridesharing.ridematching.dto.CloseDriver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Point;
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
public class RideMatchingService {

    private final DriverLocationClient locationRestClient;
    private final KafkaTemplate<String, DriverRequest> kafkaTemplate;
    private final RideClient rideRestClient;
    private final DriverRequestLockService distributedLockService;

    @KafkaListener(topics = Constants.RIDE_TOPIC, groupId = Constants.RIDE_TOPIC_GROUP)
    public void matchRider(RideMatchRequest request) {

        log.info("matching process began");

        log.info("Ride Info in matching {} ", request.getRide());
        String[] source = request.getRide().getSource().split(",");

        int secCount = 1;

        Point location = new Point(Double.parseDouble(source[0]), Double.parseDouble(source[1]));
        log.info("Match source is {} ", location);

        boolean noMatch = true;

        while (noMatch) {

            secCount++;

            List<CloseDriver> drivers = locationRestClient.getCloseDrivers(location);
            log.info("Ride matching service: close drivers within 1KM found {} ", drivers);

            if(!drivers.isEmpty()) {

                log.info("");

                noMatch = false;
                String nextDriverId = "N/A";
                if(drivers.size() > 1) nextDriverId = drivers.get(1).getId();

                // check if driver has a pending request

                var driverHasRequest = distributedLockService.driverRequestLock(drivers.get(0).getId());

                if(driverHasRequest == null) {

                    //private void notifyDriver(RideMatchRequest request, String driverId, String nextDriverId){
                    this.notifyDriver(request, drivers.get(0).getId(), nextDriverId);
                    return;

                }else{

                    // Check If there is next driver and he also has a request
                    if(!nextDriverId.equals("N/A")){

                        var nextDriverHasRequest = distributedLockService.driverRequestLock(drivers.get(1).getId());

                        if(nextDriverHasRequest == null){
                             this.notifyDriver(request, drivers.get(1).getId(), "N/A");
                             return;
                        }
                    }
                }

            }else {
                try {
                    // Delay for 5 seconds before getting close Drivers again
                    Thread.sleep(5000);

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            if(secCount > 3) {
                log.info("I can not find drivers.. stopping");
                noMatch = false;
            }
        }
    }

    private void notifyDriver(RideMatchRequest request, String driverId, String nextDriverId){

        // Lock Driver and send him Request
        this.distributedLockService.lockDriver(driverId);

        DriverRideRequestDto driverRideRequestDto = DriverRideRequestDto
                .builder()
                .rideId(request.getRide().getId())
                .driverId(driverId)
                .nextDriverId(nextDriverId)
                .status(RideRequestStatus.PENDING)
                .build();


        // save RideRequest
        rideRestClient.saveRideRequest(driverRideRequestDto);
        request.getRide().setDriverId(driverId);

        DriverRequest req = DriverRequest.builder()
                .driverId(driverId)
                .ride(request.getRide())
                .build();

        Message<DriverRequest> msg = MessageBuilder
                .withPayload(req)
                .setHeader(KafkaHeaders.TOPIC, Constants.DRIVER_REQUEST_TOPIC)
                .build();

        // Notify Driver
        kafkaTemplate.send(msg);
    }
}
