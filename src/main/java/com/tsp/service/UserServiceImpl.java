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

/**
 * Implementation of {@link UserService} interface.
 * Used for registration.
 * 
 * @author Linar Abzaltdinov
 * @version 1.0
 */

@Service
public class UserServiceImpl implements UserService {
    
    Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(User user, String role) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Set<Role> roles = new HashSet<>();
        Role r = roleDao.findByName(role);
        log.debug("SAVING: " + user.getClass() + " / " + user.getUsername()+ " / "+ role);
        roles.add(r);
        user.setRoles(roles);
        userDao.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }
}
