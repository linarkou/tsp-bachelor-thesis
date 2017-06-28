package com.tsp.model;

import com.tsp.dao.RoleDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * NOT USED
 * 
 * @author Linar Abzaltdinov
 * @version 1.0
 * 
 */
@Entity
@Table(name="Client")
public class Client extends User
{
    private Long phone;
    
    @ManyToOne
    private Place place;
    
    @OneToMany(fetch = FetchType.EAGER)
    private List<Order> orders = new ArrayList<>();

    public Client()
    {
    }
    /**
     * @return the phone
     */
    public Long getPhone()
    {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(Long phone)
    {
        this.phone = phone;
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
        //this.address = place.getAddress();
    }

//    /**
//     * @return the address
//     */
//    public String getAddress()
//    {
//        return address;
//    }
//
//    /**
//     * @param address the address to set
//     */
//    public void setAddress(String address)
//    {
//        this.address = address;
//        setPlace(new Place(address));
//    }

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
