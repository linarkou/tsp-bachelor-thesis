/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.service;

import com.tsp.dao.PlaceDao;
import com.tsp.geocode.Geocoder;
import com.tsp.model.LatLng;
import com.tsp.model.Place;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for {@link com.tsp.model.Place}
 * 
 * @author Linar Abzaltdinov
 */
@Service
public class PlaceService
{
    @Autowired
    private PlaceDao placeDao;
    
    @Transactional
    public Place addNewPlace(String address)
    {
        Map.Entry<String, LatLng> geocode = Geocoder.geocode(address);
        if (geocode.getKey()=="")
            return null;
        
        try {
            //return existing entity
            return placeDao.findOne(geocode.getValue());
        } catch (IllegalArgumentException iae)
        {
            //create new entity
            Place p = new Place(geocode.getKey(), geocode.getValue());
            p = placeDao.save(p);
            return p;
        }
    }
    
    public Place findPlaceById(LatLng id)
    {
        return placeDao.findOne(id);
    }
}
