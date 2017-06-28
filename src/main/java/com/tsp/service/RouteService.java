/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.service;

import com.tsp.dao.RouteDao;
import com.tsp.model.Driver;
import com.tsp.model.Order;
import com.tsp.model.Route;
import com.tsp.solver.Solver;
import java.util.List;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for {@link com.tsp.model.Route}
 * 
 * @author Linar Abzaltdinov
 */
@Service
public class RouteService
{
    Logger log = LoggerFactory.getLogger(RouteService.class);
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private RouteDao routeDao;
    
    @Transactional
    public boolean createRoute(LocalDate date, Driver driver)
    {
        List<Order> orders = orderService.findUntakenOrdersByDateForDriver(date, driver);
        if (orders.size() == 0)
        {
            return false;
        }
        List<Order> ordersAsRoute = Solver.getOrdersAsRoute(driver.getStock(), orders);
        Route route = new Route(driver, date, ordersAsRoute);
        if (routeDao.saveRoute(route))
        {
            log.info("New route created for " + route.getDriver().getUsername() + " with id=" + route.getId());
            return true;
        }
        else
            return false;
    }

    public List<Route> findRouteByDriver(Driver driver)
    {
        return routeDao.findRouteByDriver(driver);
    }

    public Route findById(Long id)
    {
        return routeDao.findById(id);
    }

    public void complete(Long id)
    {
        routeDao.complete(id);
    }

    public Route getUncompletedRouteByDriver(Driver driver)
    {
        return routeDao.getUncompletedRouteByDriver(driver);
    }
    
    
}
