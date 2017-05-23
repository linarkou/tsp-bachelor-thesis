package com.tsp.service;

import com.tsp.model.Client;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for {@link Driver}
 *
 * @author Linar Abzaltdinov
 * @version 1.0
 */

public interface ClientService {
    @Transactional
    void save(Client client);

    Client findByUsername(String username);
}
