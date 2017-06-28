package com.tsp.dao;

import com.tsp.model.*;
import com.tsp.service.PlaceService;
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
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class OrderDao
{
    @PersistenceContext
    EntityManager em;
    
    @Autowired
    ClientDao clientDao;
    
    @Autowired
    PlaceService placeSerivce;
    
    @Autowired
    StockDao stockDao;
    
    public OrderDao()
    {
    }
    
    @Transactional
    public void cancelOrder(Long id)
    {
        Order found = findById(id);
        if (found != null)
            found.cancel();
    }
    
    public List<Order> findUntakenOrdersByDateForDriver(LocalDate date, Driver dr)
    {
        Stock stock = dr.getStock();
        return em.createQuery("select o from Stock s LEFT JOIN s.orders o where s = :stock AND o.date = :date AND o.inRoute = FALSE",Order.class)
                .setParameter("date", date)
                .setParameter("stock", stock)
                .getResultList();
    }
    
    public List<Order> findAllOrders()
    {
        return em.createQuery("select c from Order c order by c.id",Order.class)
                .getResultList();
    }
    
    public void save(Order order)
    {
        em.persist(order);
    }
    
    public Order findById(Long id)
    {
        return em.find(Order.class, id);
    }
    
    public List<Order> findOrdersByClientId(Long id)
    {
        Client cl = clientDao.findById(id);
        return cl.getOrders(); 
    }
    
    public List<Order> findOrdersByClient(Client client)
    {
        return client.getOrders(); 
    }

    @Transactional
    public void removeById(Long id)
    {
        Order found = this.findById(id);
        if (found != null)
            em.remove(found);
    }
    
}
