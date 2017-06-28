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
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 *
 * @author Linar Abzaltdinov
 */

public interface PlaceDao extends JpaRepository<Place, LatLng>
{
}
