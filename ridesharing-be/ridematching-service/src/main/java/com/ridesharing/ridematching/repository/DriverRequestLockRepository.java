package com.ridesharing.ridematching.repository;

import com.ridesharing.ridematching.model.DriverRequestLock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface DriverRequestLockRepository extends CrudRepository<DriverRequestLock, String>, QueryByExampleExecutor<DriverRequestLock> {
    DriverRequestLock findByDriverId(String driverId);
}
