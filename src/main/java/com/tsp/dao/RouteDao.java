/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.dao;

import com.tsp.model.Route;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author polzovatel
 */
@Repository
@Transactional
public class RouteDao
{
    @PersistenceContext
    EntityManager em;
    
    public RouteDao() {}
    
    public void saveRoute(Route r)
    {
        em.persist(r);
    }
    
}
