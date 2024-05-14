package util;

import com.ridesharing.core.dto.RideDto;
import com.ridesharing.rideservice.model.Ride;

public class Utility {
    public static RideDto ConvertRideToDto(Ride ride){
        var rideDto = new RideDto();
        rideDto.setId(ride.getId());
        rideDto.setRiderId(ride.getRiderId());
        rideDto.setDriverId(ride.getDriverId());
        rideDto.setRiderName(ride.getRiderName());
        rideDto.setDriverName("N/A");
        rideDto.setFare(ride.getFare());
        rideDto.setSource(ride.getSource().getX() + "," + ride.getSource().getY());
        rideDto.setDestination(ride.getDestination().getX() + "," + ride.getDestination().getY());
        rideDto.setStatus(ride.getStatus());
        rideDto.setDate(ride.getDate().toString());
        return rideDto;
    }
}
