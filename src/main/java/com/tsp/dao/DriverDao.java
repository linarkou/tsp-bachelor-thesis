package com.tsp.dao;

import com.tsp.model.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

//public interface DriverDao extends JpaRepository<Driver, Long> {
//    Driver findByUsername(String username);
//}

@Repository
@Transactional
public class DriverDao
{
    @PersistenceContext
    EntityManager em;
    
    public void save(Driver driver)
    {
        em.persist(driver);
    }
    
    public List<Route> getRoutesByUsername(String username)
    {
        Driver dr = this.findByUsername(username);
        return dr.getRoutes();
    }
    
    public List<Route> getRoutesByDriver(Driver dr)
    {
        return em.createQuery("select r FROM Driver d LEFT JOIN d.routes r where d.id = :id", Route.class).setParameter("id", dr.getId()).getResultList();
    }
    
    public List<Driver> getAllDrivers()
    {
        List<Driver> resultList = em.createQuery("select d from Driver d order by id", Driver.class).getResultList();
        return resultList;
    }
    
    public Driver findById(Long id)
    {
        return em.find(Driver.class, id);
    }
    
    public Driver findByUsername(String username)
    {
        try {
            TypedQuery<Driver> query = em.createQuery("select d from Driver d where d.username=:name", Driver.class).setParameter("name", username);
            return query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        } catch (NonUniqueResultException nure) {
            return null; 
        } 
    }

    @Transactional
    public void removeById(Long id)
    {
        Driver found = this.findById(id);
        if (found != null)
            em.remove(found);
    }
}
