/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.dao;

import com.tsp.model.Driver;
import com.tsp.model.Order;
import com.tsp.model.Route;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    Logger log = LoggerFactory.getLogger(RouteDao.class);
    
    @PersistenceContext
    EntityManager em;
    
    public RouteDao() {}
    
    @Transactional
    public boolean saveRoute(Route r)
    {
        try {
            em.persist(r);
            for (Order o : this.getOrdersOfRoute(r))
                o.putToRoute();
            return true;
        } catch (Exception ex)
        {
            log.warn(ex.getMessage());
            return false;
        }
    }
    
    public List<Order> getOrdersOfRoute(Route r)
    {
        return em.createQuery("select o from Route r LEFT JOIN r.orders o where r.id = :id", Order.class).setParameter("id", r.getId()).getResultList();
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
    
    public Route findById(Long id)
    {
        return em.find(Route.class, id);
    }
    
    public List<Route> findRouteByDriver(Driver driver)
    {
        return em.createQuery("select r FROM Route r where r.driver = :driver", Route.class).setParameter("driver", driver).getResultList();
    }
    
    public void complete(Long id)
    {
        this.findById(id).complete();
    }
    
}
