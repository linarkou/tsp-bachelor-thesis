/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.dao;

import com.tsp.model.Place;
import com.tsp.model.Stock;
import com.tsp.service.PlaceService;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 *
 * @author Linar Abzaltdinov
 */
@Repository
public class StockDao
{
    Logger log = LoggerFactory.getLogger(StockDao.class);

    @PersistenceContext
    EntityManager em;
    
    @Autowired
    PlaceService placeService;
    
    public StockDao()
    {
    }
    
    public Stock findStockById(Long id)
    {
        return em.find(Stock.class, id);
    }
    
    public Stock findStockByPlace(Place p)
    {
        TypedQuery<Stock> query = em.createQuery("select s from Stock s where s.place = ?1",Stock.class).setParameter(1, p);
        try {
            return query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        } catch (NonUniqueResultException nure) {
            return null; 
        }
    }
    @Transactional
    public Stock addNewStock(String address)
    {
        Stock s = null;
        Place place = placeService.addNewPlace(address);
        return addNewStock(place);
    }
    
    @Transactional
    public Stock addNewStock(Place place)
    {
        Stock found = findStockByPlace(place);
        if (found != null)
            return found;
        Stock s = new Stock(place);
        em.persist(s);
        return s;
    }
    
    public List<Stock> getAllStocks()
    {
        List<Stock> resultList = em.createQuery("select s from Stock s order by id", Stock.class).getResultList();
        return resultList;
    }
    
    @Transactional
    public void removeStockById(Long id)
    {
        Stock res = findStockById(id);
        em.remove(res);
    }
}
