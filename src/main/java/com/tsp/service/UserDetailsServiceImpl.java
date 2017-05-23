package com.tsp.service;

import com.tsp.dao.ClientDao;
import com.tsp.dao.DriverDao;
import com.tsp.dao.UserDao;
import com.tsp.model.Driver;
import com.tsp.model.Role;
import com.tsp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link org.springframework.security.core.userdetails.UserDetailsService} interface.
 * Used for log in
 * 
 * @author Linar Abzaltdinov
 * @version 1.0
 */


public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private DriverDao driverDao;
    @Autowired
    private ClientDao clientDao;
    
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug(String.format("Checking user %s / loadUserByUsername", username));

        User user = driverDao.findByUsername(username);
        if (user == null)
            user = userDao.findByUsername(username);
        if (user == null)
            user = clientDao.findByUsername(username);
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        for (Role role : user.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
