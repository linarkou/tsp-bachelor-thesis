/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.controllers;

import com.tsp.dao.ClientDao;
import com.tsp.dao.DriverDao;
import com.tsp.dao.OrderDao;
import com.tsp.dao.RouteDao;
import com.tsp.dao.StockDao;
import com.tsp.ga.GAService;
import com.tsp.model.Client;
import com.tsp.model.Driver;
import com.tsp.model.Order;
import com.tsp.model.Route;
import java.security.Principal;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Linar Abzaltdinov
 */
@Controller
@RequestMapping(value = "/driver")
@RolesAllowed(value = "ROLE_DRIVER")
@SessionAttributes(types = {Driver.class, Route.class})
public class DriverController
{
    @Autowired
    ClientDao clientDao;
    
    @Autowired
    RouteDao routeDao;
    
    @Autowired
    DriverDao driverDao;
    
    @Autowired
    OrderDao orderDao;
    
    @RequestMapping(value = {"/index", "/"}, method = RequestMethod.GET)
    public String driver(Model model, Principal principal) {
        String username = principal.getName();
        Driver dr = driverDao.findByUsername(username);
        model.addAttribute(dr);
        return "welcome";
    }
    
    @RequestMapping(value="/routes")
    String routes(Driver driver, Model model) 
    {
        List<Route> allRoutes = driverDao.getRoutesByDriver(driver);
        model.addAttribute("routes", allRoutes);
        return "routes";
    }
    
    @RequestMapping(value="/currentRoute")
    String checkForCurrentRoute(Principal principal, Model model) 
    {
        String username = principal.getName();
        Driver driver = driverDao.findByUsername(username);
        Route uncompletedRoute = routeDao.getUncompletedRouteByDriver(driver);
        if (uncompletedRoute == null) 
        {
            List<Order> orders = orderDao.findUntakenOrdersByDate(LocalDate.now());
            model.addAttribute("orderscount", orders.size());
            return "suggestnewroute";
        } else 
        {
            model.addAttribute("route", uncompletedRoute.toStringForYMaps());
            model.addAttribute("orderlist", uncompletedRoute.getOrders());
            return "currentroute";
        }
    }
    
    @RequestMapping(value="/initroute")
    String initroute(Principal principal, Model model) 
    {
        String username = principal.getName();
        Driver driver = driverDao.findByUsername(username);
        List<Order> orders = orderDao.findUntakenOrdersByDate(LocalDate.now());
        for (Order o : orders)
            o.putToRoute();
        String places = GAService.generateRouteForGettingLengths(driver.getStock(), orders);
        Route furuteRoute = new Route(driver, LocalDate.now(), orders);
        model.addAttribute("places", places);
        model.addAttribute(furuteRoute);
        return "initroute";
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/buildroute")
    public String routeCalc(Model model, Route route, @RequestParam("lengths") String lengths, SessionStatus status) 
    {
        List<Order> ordersAsRoute = GAService.getOrdersAsRoute(route.getDriver().getStock(), route.getOrders(), lengths);
        route.setOrders(ordersAsRoute);
        routeDao.saveRoute(route);
        return "redirect:/driver/currentRoute";
        //String[] split = lengths.split(",");
        //ArrayList<ArrayList<Integer>> a = new ArrayList<>();
        //return "redirect:/route";
    }
    
    
    
}
