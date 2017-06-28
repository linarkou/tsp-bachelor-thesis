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
    @JoinTable(name = "route_order", joinColumns = {
			@JoinColumn(name = "route_id", nullable = false, updatable = false) },
			inverseJoinColumns = { @JoinColumn(name = "order_id",
					nullable = false, updatable = false, unique = true) })
    @OrderColumn(name="ORDER_IN_ROUTE")
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
        if (orders == null || orders.isEmpty())
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
            sb.append("\', \'");
        }
        sb.append(this.driver.getStock().getPlace()).append("\'");
        return sb.toString();
    }
    
    public String toStringForYNavi()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("lat_from=").append(this.driver.getStock().getPlace().getLocation().getLat());
        sb.append("&lon_from=").append(this.driver.getStock().getPlace().getLocation().getLng());
        sb.append("&lat_to=").append(orders.get(orders.size()-1).getPlace().getLocation().getLat());
        sb.append("&lon_to=").append(orders.get(orders.size()-1).getPlace().getLocation().getLng());
        for (int i = 0; i < orders.size(); ++i)
        {
            sb.append("&lat_via_"+i+"=").append(orders.get(i).getPlace().getLocation().getLat());
            sb.append("&lon_via_"+i+"=").append(orders.get(i).getPlace().getLocation().getLng());
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
