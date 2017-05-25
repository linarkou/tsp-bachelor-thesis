/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.dao;

import com.tsp.model.Driver;
import com.tsp.model.Route;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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
    
    public Route getUncompletedRouteByDriver(Driver dr)
    {
        try {
            TypedQuery<Route> query = em.createQuery("select r from Route r where r.driver = :driver AND r.completed = FALSE", Route.class).setParameter("driver", dr);
            return query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        } catch (NonUniqueResultException nure) {
            return null; 
        } 
    }
    
}
