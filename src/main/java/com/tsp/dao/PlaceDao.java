/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.dao;

import com.tsp.geocode.Geocoder;
import com.tsp.model.LatLng;
import com.tsp.model.Place;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 *
 * @author Linar Abzaltdinov
 */
@Repository
@Transactional
public class PlaceDao
{
    Logger log = LoggerFactory.getLogger(PlaceDao.class);
    
    @PersistenceContext
    EntityManager em;
    
    public PlaceDao()
    {
    }
    
    public void save(Place p)
    {
        em.persist(p);
    }
    
    public Place findPlaceById(LatLng ll)
    {
        return em.find(Place.class, ll);
    }
    
    public Place addNewPlace(String address)
    {
        Map.Entry<String, LatLng> geocode = Geocoder.geocode(address);
        if (geocode.getKey()=="")
            return null;
        Place foundPlace = findPlaceById(geocode.getValue());
        if (foundPlace != null)
            return foundPlace;
        Place p = new Place(geocode.getKey(), geocode.getValue());
        try {
            em.persist(p);
        } catch (Exception ex)
        {
            return null;
        }
        return p;
    }
    
    public List<Place> getAllPlaces()
    {
        List<Place> resultList = em.createQuery("select p from Place p", Place.class).getResultList();
        return resultList;
    }
    
    
}
