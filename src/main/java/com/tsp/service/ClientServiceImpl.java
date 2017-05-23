package com.tsp.service;

import com.tsp.dao.ClientDao;
import com.tsp.dao.DriverDao;
import com.tsp.dao.PlaceDao;
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
 * @author Eugene Suleimanov
 * @version 1.0
 */

@Service
public class ClientServiceImpl implements ClientService 
{
    Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);
    @Autowired
    private ClientDao clientDao;
    
    @Autowired
    PlaceDao placeDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional
    public void save(Client user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Set<Role> roles = new HashSet<>();
        String role = "ROLE_CLIENT";
        Role r = roleDao.findByName(role);
        log.debug("SAVING: " + user.getClass() + " / " + user.getUsername()+ " / "+ role);
        roles.add(r);
        user.setRoles(roles);
        placeDao.save(user.getPlace());
        clientDao.save(user);
    }

    @Override
    public Client findByUsername(String username) {
        return clientDao.findByUsername(username);
    }
}
