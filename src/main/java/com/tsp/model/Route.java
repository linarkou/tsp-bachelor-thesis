/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.model;

import java.util.List;
import javax.persistence.*;
import org.joda.time.LocalDate;

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
    private List<Order> orders;   
    
    @ManyToOne
    private Driver driver;
    
    private LocalDate date;
    
    private boolean completed = false;
    
    public Route() { }
    
    public Route(Driver driver, LocalDate date, List<Order> orders) 
    { 
        this.driver = driver;
        this.date = date;
        this.orders = orders;
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
        if (orders.isEmpty())
            return "Empty route";
        
        StringBuilder sb = new StringBuilder("Date of route: " + this.getDate() + "\n\'");
        for (int i = 0; i < orders.size(); ++i)
        {
            sb.append(orders.get(i));
            if (i != orders.size()-1)
                sb.append("\', \'");
            else
                sb.append("\'");
        }
        return sb.toString();
    }
    
    public String toStringForYMaps()
    {
        StringBuilder sb = new StringBuilder("\'");
        sb.append(this.driver.getStock().getPlace()).append("\', \'");
        for (int i = 0; i < orders.size(); ++i)
        {
            sb.append(orders.get(i).getPlace());
            if (i == orders.size()-1)
                sb.append("\'");
            else
                sb.append("\', \'");
        }
        
        return sb.toString();
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

    /**
     * @return the completed
     */
    public boolean isCompleted()
    {
        return completed;
    }
    
    public void complete()
    {
        this.completed = true;
        for (Order o : orders)
        {
            o.complete();
        }
    }
}
