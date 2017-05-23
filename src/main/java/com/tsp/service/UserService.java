package com.tsp.service;

import com.tsp.model.Role;
import com.tsp.model.User;
import java.util.Set;

/**
 * Service class for {@link User}
 *
 * @author Linar Abzaltdinov
 * @version 1.0
 */

public interface UserService {

    void save(User user, String role);

    User findByUsername(String username);
}
