/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.*;

@Entity
@Table(name = "Stock", uniqueConstraints={@UniqueConstraint(columnNames={"place_lat", "place_lng"})})
public class Stock
{   
    @OneToOne
    @JoinColumns({
        @JoinColumn(name = "place_lat", referencedColumnName = "lat"),
        @JoinColumn(name = "place_lng", referencedColumnName = "lng")
    })
    private Place place;
    
    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Driver> drivers;
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<Order> orders;
    
    @Id
    @GeneratedValue
    private Long id;
    
    public Stock()
    {
    }
    
    public Stock(Place p)
    {
        this.place = p;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    /**
     * @return the place
     */
    public Place getPlace()
    {
        return place;
    }

    /**
     * @param place the place to set
     */
    public void setPlace(Place place)
    {
        this.place = place;
    }

    /**
     * @return the drivers
     */
    public List<Driver> getDrivers()
    {
        return drivers;
    }

    /**
     * @param drivers the drivers to set
     */
    public void setDrivers(List<Driver> drivers)
    {
        this.drivers = drivers;
    }

    /**
     * @return the orders
     */
    public List<Order> getOrders()
    {
        return orders;
    }

    /**
     * @param orders the orders to set
     */
    public void setOrders(List<Order> orders)
    {
        this.orders = orders;
    }
    
}
