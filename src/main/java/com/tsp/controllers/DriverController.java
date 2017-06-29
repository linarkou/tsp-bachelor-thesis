/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.controllers;

import com.tsp.dao.ClientDao;
import com.tsp.dao.DriverDao;
import com.tsp.dao.RouteDao;
import com.tsp.model.Driver;
import com.tsp.model.Route;
import com.tsp.service.DriverService;
import com.tsp.service.OrderService;
import com.tsp.service.RouteService;
import java.security.Principal;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Linar Abzaltdinov
 */
@Controller
@RolesAllowed(value = "ROLE_DRIVER")
@RequestMapping(value = "/driver")
public class DriverController
{    
    Logger log = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    RouteService routeSerivce;
    
    @Autowired
    DriverService driverDao;
    
    @Autowired
    OrderService orderSerivce;
    
    @RequestMapping(value = {"/index", "/"}, method = RequestMethod.GET)
    public String driver(Model model, Principal principal) {
        String username = principal.getName();
        Driver dr = driverDao.findByUsername(username);
        model.addAttribute(dr);
        return "welcome";
    }
    
    @RequestMapping(value="/routes")
    String routes(Principal principal, Model model) 
    {
        String username = principal.getName();
        Driver driver = driverDao.findByUsername(username);
        List<Route> allRoutes = routeSerivce.findRouteByDriver(driver);
        model.addAttribute("routes", allRoutes);
        return "routes";
    }
    
    @RequestMapping(method = RequestMethod.GET, value="/routes/show")
    String showRoute(@RequestParam("id") Long id, Model model) 
    {
        Route route = routeSerivce.findById(id);
        model.addAttribute("route", route.toStringForYMaps());
        model.addAttribute("yandexnaviroute", route.getOrders());
        return "routeonmap";
    }
    
    @RequestMapping(value="/currentRoute")
    String checkForCurrentRoute(Principal principal, Model model) 
    {
        String username = principal.getName();
        Driver driver = driverDao.findByUsername(username);
        Route uncompletedRoute = routeSerivce.getUncompletedRouteByDriver(driver);
        if (uncompletedRoute == null) 
        {
            return "noroute";
        } else 
        {
            model.addAttribute("route", uncompletedRoute.toStringForYMaps());
            model.addAttribute("yandexnaviroute", uncompletedRoute.toStringForYNavi());
            return "routeonmap";
        }
    }
}
//    @RequestMapping(value="/initroute")
//    String initroute(Principal principal, Model model) 
//    {
//        String username = principal.getName();
//        Driver driver = driverDao.findByUsername(username);
//        List<Order> orders = orderDao.findUntakenOrdersByDateForDriver(LocalDate.now(), driver);
//        for (Order o : orders)
//            o.putToRoute();
//        String places = Solver.generateRouteForGettingLengths(driver.getStock(), orders);
//        Route furuteRoute = new Route(driver, LocalDate.now(), orders);
//        model.addAttribute("places", places);
//        model.addAttribute(furuteRoute);
//        return "initroute";
//    }
//    
//    @RequestMapping(method = RequestMethod.POST, value = "/buildroute")
//    public String routeCalc(Model model, Route route, @RequestParam("lengths") String lengths, SessionStatus status) 
//    {
//        List<Order> ordersAsRoute = Solver.getOrdersAsRoute(route.getDriver().getStock(), route.getOrders(), lengths);
//        route.setOrders(ordersAsRoute);
//        
//        routeDao.saveRoute(route);
//        log.info("New route created for " + route.getDriver().getUsername() + " with id=" + route.getId());
//        return "redirect:/driver/currentRoute";
//        //String[] split = lengths.split(",");
//        //ArrayList<ArrayList<Integer>> a = new ArrayList<>();
//        //return "redirect:/route";
//    }
//    
//    
//    

