package com.ridesharing.ridematching.service;

import com.ridesharing.core.dto.DriverRequestLockDto;
import com.ridesharing.ridematching.model.DriverRequestLock;
import com.ridesharing.ridematching.repository.DriverRequestLockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverRequestLockService {

    private final DriverRequestLockRepository repository;

    public void lockDriver(String driverId) {
        this.repository.save(DriverRequestLock.builder()
                        .driverId(driverId)
                .build());
    }

    public void unlockDriver(String driverId){
        var theLock = this.repository.findById(driverId);
        theLock.ifPresent(driverRequestLock -> this.repository.deleteById(driverRequestLock.getId()));
    }

    public DriverRequestLock driverRequestLock(String driverId){
        return this.repository.findByDriverId(driverId);
    }

    public DriverRequestLockDto getLockForDriver(String id){
        var lock = this.repository.findByDriverId(id);
        if(lock != null){
            return DriverRequestLockDto.builder().driverId(id).id(lock.getId()).build();
        }

        return null;
    }
}
