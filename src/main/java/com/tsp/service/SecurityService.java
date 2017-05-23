package com.tsp.service;

/**
 * Service for Security.
 *
 * @author Linar Abzaltdinov
 * @version 1.0
 */

public interface SecurityService {

    String findLoggedInUsername();

    void autoLogin(String username, String password);
}
