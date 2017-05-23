package com.tsp.service;

import com.tsp.model.Driver;

/**
 * Service class for {@link Driver}
 *
 * @author Linar Abzaltdinov
 * @version 1.0
 */

public interface DriverService {

    void save(Driver driver);

    Driver findByUsername(String username);
}
