/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.model;

import javax.persistence.*;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Linar Abzaltdinov
 */
@Entity
@Table(name = "Orders")
public class Order
{
    @Id
    @GeneratedValue
    private Long id;
    
    @ManyToOne
    private Place place;
    
    @Column(name = "status")
    private String status = "Не выполнен";
    
    /**
     * Дата, когда нужно выполнить заказ
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;
    
    
    private boolean inRoute = false;
    /**
     * Описание заказа
     */
    private String description;
    
    public Order() { }
    
    public Order(Place place, LocalDate date, String desc) 
    { 
        this.place = place;
        this.date = date;
        this.description = desc;
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
     * @return the Status
     */
    public String getStatus()
    {
        return this.status;
    }

    public void complete()
    {
        this.status = "Выполнен";
        this.inRoute = true;
    }
    
    public void cancel()
    {
        this.status = "Отменен";
        this.inRoute = true;
    }
    
    public boolean isCompleted()
    {
        return this.status.equals("Выполнен");
    }

    /**
     * @return the description
     */
    public String getDescription()
    {
        return new String(description);
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description)
    {
        this.description = description;
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
     * @return the inRoute
     */
    public boolean isInRoute()
    {
        return inRoute;
    }
    
    public void putToRoute()
    {
        this.inRoute = true;
    }
    
}
