package com.tsp.service;

import com.tsp.dao.ClientDao;
import com.tsp.dao.DriverDao;
import com.tsp.dao.RoleDao;
import com.tsp.dao.UserDao;
import com.tsp.model.Client;
import com.tsp.model.Driver;
import com.tsp.model.Role;
import com.tsp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link UserService} interface.
 * Used for registration.
 * 
 * @author Linar Abzaltdinov
 * @version 1.0
 */

@Service
public class DriverServiceImpl implements DriverService 
{
    
    Logger log = LoggerFactory.getLogger(DriverServiceImpl.class);
    @Autowired
    private DriverDao driverDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    //@Override
    @Transactional
    public void save(Driver driver) {
        driver.setPassword(bCryptPasswordEncoder.encode(driver.getPassword()));
        Set<Role> roles = new HashSet<>();
        Role r = roleDao.getOne(2L);
        log.debug("SAVING: " + driver.getClass() + " / " + driver.getUsername()+ " / "+ r.getName());
        roles.add(r);
        driver.setRoles(roles);
        driverDao.save(driver);
    }

    //@Override
    public Driver findByUsername(String username) {
        return driverDao.findByUsername(username);
    }
}
