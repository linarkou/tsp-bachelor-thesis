/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.model;

import com.tsp.dao.RoleDao;
import com.tsp.dao.StockDao;
import java.util.HashSet;
import java.util.List;
import javax.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Table(name="Driver")
public class Driver extends User
{
    /**
     * Склад, на котором работает водитель
     */
    @ManyToOne
    private Stock stock;
    
    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Route> routes;
    
    public Driver()
    {
        Logger log = LoggerFactory.getLogger(User.class);
        log.debug("DRIVER CONSTRUCTOR");
    }
    
    /**
     * @return the stock
     */
    public Stock getStock()
    {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(Stock stock)
    {
        this.stock = stock;
    }

    /**
     * @return the routes
     */
    public List<Route> getRoutes()
    {
        return routes;
    }

    /**
     * @param routes the routes to set
     */
    public void setRoutes(List<Route> routes)
    {
        this.routes = routes;
    }
    
}
