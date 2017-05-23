/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author Linar Abzaltdibov
 * @version 1.0
 */
@Entity
@Table(name="Route")
public class Route
{
    @Id
    @GeneratedValue
    private Long id;
    
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Place> places;   
    
    @ManyToOne
    private Driver driver;
    
    private LocalDate date;
    
    public Route() { }
    
    public Route(Driver driver, LocalDate date, List<Place> places) 
    { 
        this.driver = driver;
        this.date = date;
        this.places = places;
    }
    
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }
    
    @Override
    public String toString()
    {
        if (places.isEmpty())
            return "Empty route";
        
        StringBuilder sb = new StringBuilder("Date of route: " + this.getDate() + "\n\'");
        for (int i = 0; i < places.size(); ++i)
        {
            sb.append(places.get(i));
            if (i != places.size()-1)
                sb.append("\', \'");
            else
                sb.append("\'");
        }
        return sb.toString();
    }

    /**
     * @return the places
     */
    public List<Place> getPlaces()
    {
        return places;
    }

    /**
     * @param places the places to set
     */
    public void setPlaces(List<Place> places)
    {
        this.places = places;
    }

    /**
     * @return the driver
     */
    public Driver getDriver()
    {
        return driver;
    }

    /**
     * @param driver the driver to set
     */
    public void setDriver(Driver driver)
    {
        this.driver = driver;
    }

    /**
     * @return the date
     */
    public LocalDate getDate()
    {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(LocalDate date)
    {
        this.date = date;
    }
}
