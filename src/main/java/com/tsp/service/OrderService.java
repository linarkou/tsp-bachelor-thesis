/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.service;

import com.tsp.dao.OrderDao;
import com.tsp.dao.StockDao;
import com.tsp.model.Driver;
import com.tsp.model.Order;
import com.tsp.model.Place;
import com.tsp.model.Stock;
import com.tsp.service.PlaceService;
import java.util.List;
import java.util.Map;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for {@link com.tsp.model.Order}
 * 
 * @author Linar Abzaltdinov
 */
public class OrderService
{
    @Autowired
    private OrderDao orderDao;
    
    @Autowired
    private PlaceService placeSerivce;
    
    @Autowired
    private StockDao stockDao;
    
    @Transactional
    public Order addNewOrder(String address, LocalDate date, String description)
    {
        Place place = placeSerivce.addNewPlace(address);
        return addNewOrder(place, date, description);
    }
    
    public Order addNewOrder(Place place, LocalDate date, String description)
    {
        Order order = new Order(place, date, description);
        orderDao.save(order);
        List<Stock> allStocks = stockDao.getAllStocks();
        Stock stock = null;
        double min = 999999999;
        for (Stock s : allStocks)
        {
            double haversineDist = s.getPlace().getLocation().haversineDist(place.getLocation());
            if (haversineDist < min)
            {
                min = haversineDist;
                stock = s;
            }
        }
        if (stock != null)
        {
            stock.getOrders().add(order);
        }
        return order;
    }

    public List<Order> findOrdersByClientId(Long id)
    {
        return orderDao.findOrdersByClientId(id);
    }

    public List<Order> findAllOrders()
    {
        return orderDao.findAllOrders();
    }

    public void cancelOrder(Long id)
    {
        orderDao.cancelOrder(id);
    }
    
    public List<Order> findUntakenOrdersByDateForDriver(LocalDate date, Driver driver)
    {
        return orderDao.findUntakenOrdersByDateForDriver(date, driver);
    }
    
    
}
