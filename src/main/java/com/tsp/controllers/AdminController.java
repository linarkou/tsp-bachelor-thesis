/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.controllers;

import com.tsp.dao.ClientDao;
import com.tsp.dao.DriverDao;
import com.tsp.dao.OrderDao;
import com.tsp.dao.PlaceDao;
import com.tsp.dao.RouteDao;
import com.tsp.dao.StockDao;
import com.tsp.model.Client;
import com.tsp.model.Driver;
import com.tsp.model.LatLng;
import com.tsp.model.Order;
import com.tsp.model.Place;
import com.tsp.model.Route;
import com.tsp.model.Stock;
import com.tsp.service.DriverService;
import com.tsp.service.OrderService;
import com.tsp.service.PlaceService;
import com.tsp.service.RouteService;
import com.tsp.solver.Solver;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
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
@RequestMapping(value = "/admin")
@RolesAllowed(value = "ROLE_ADMIN")
@SessionAttributes({"futureRoute"})
public class AdminController {
    private static final Logger log = LoggerFactory.getLogger(AdminController.class);
    
    @Autowired
    ClientDao clientDao;
    
    @Autowired
    StockDao stockDao;
    
    @Autowired
    DriverDao driverDao;
    
    @Autowired
    PlaceService placeService;
    
    @Autowired
    RouteService routeService;
    
    @Autowired
    DriverService driverService;
    
    @Autowired
    OrderService orderService;
    
    HashSet<Place> places = new HashSet<>();
        
    @RequestMapping(value = {"/index", "/"}, method = RequestMethod.GET)
    public String admin(Model model) {
        return "welcome";
    }
    
    @RequestMapping(value="/stocks")
    ModelAndView stocks() 
    {
        List<Stock> allStocks = stockDao.getAllStocks();
        ModelAndView mv = new ModelAndView("stocks");
        mv.addObject("stocks", allStocks);
        return mv;
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/stocks/add")
    String addStock(@RequestParam("address") String address) 
    {
        stockDao.addNewStock(address);
        return "redirect:/admin/stocks";
    }
    
    @RequestMapping(method = RequestMethod.GET, value="/stocks/remove")
    String removeStock(@RequestParam("id") Long id) 
    {
        stockDao.removeStockById(id);
        return "redirect:/admin/stocks";
    }
    
    @RequestMapping(value="/drivers")
    ModelAndView drivers() 
    {
        boolean canCreate = true;
        if (stockDao.getAllStocks().isEmpty())
            canCreate = false;
        List<Driver> allDrivers = driverDao.getAllDrivers();
        ModelAndView mv = new ModelAndView("drivers");
        mv.addObject("canCreate", canCreate);
        mv.addObject("drivers", allDrivers);
        return mv;
    }
    
    @RequestMapping(method = RequestMethod.GET, value="/drivers/remove")
    String removeDriver(@RequestParam("id") Long id) 
    {
        driverDao.removeById(id);
        return "redirect:/admin/drivers";
    }
    
    @RequestMapping(method = RequestMethod.GET, value="/drivers/routes")
    String showRoutesOfDriver(@RequestParam("id") Long id, Model model) 
    {
        Driver driver = driverDao.findById(id);
        model.addAttribute("username", driver.getUsername());
        model.addAttribute("routes", routeService.findRouteByDriver(driver));
        return "routes";
    }
    
    @RequestMapping(method = RequestMethod.GET, value="/drivers/routes/showroute")
    String showRoute(@RequestParam("id") Long id, Model model) 
    {
        Route route = routeService.findById(id);
        model.addAttribute("route", route.toStringForYMaps());
        model.addAttribute("yandexnaviroute", route.getOrders());
        return "routeonmap";
    }
    
    @RequestMapping(method = RequestMethod.GET, value="/drivers/routes/complete")
    String completeRoute(@RequestParam("id") Long id, Model model) 
    {
        routeService.complete(id);
        return "redirect:/admin/drivers";
    }
    
    @RequestMapping(value="/clients")
    ModelAndView clients() 
    {
        List<Client> allClients = clientDao.getAllClients();
        ModelAndView mv = new ModelAndView("clients");
        mv.addObject("clients", allClients);
        return mv;
    }
    
    @RequestMapping(method = RequestMethod.GET, value="/clients/remove")
    String removeClient(@RequestParam("id") Long id) 
    {
        clientDao.removeById(id);
        return "redirect:/admin/clietns";
    }
    
    @RequestMapping(method = RequestMethod.GET, value="/clients/orders")
    ModelAndView getOrdersForClient(@RequestParam("clientid") Long id) 
    {
        List<Order> orders = orderService.findOrdersByClientId(id);
        ModelAndView mv = new ModelAndView("orders");
        mv.addObject("orders", orders);
        mv.addObject("username", clientDao.findById(id).getUsername());
        return mv;
    }
    
    @RequestMapping(value="/orders")
    ModelAndView orders() 
    {
        List<Order> allOrders = orderService.findAllOrders();
        ModelAndView mv = new ModelAndView("orders");
        mv.addObject("orders", allOrders);
        mv.addObject("today", LocalDate.now());
        return mv;
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/orders/add")
    String addOrder(@RequestParam("address") String address, @RequestParam("desc") String description, @RequestParam("date") @DateTimeFormat(iso=ISO.DATE) LocalDate date) 
    {
        orderService.addNewOrder(address, date, description);
        return "redirect:/admin/orders";
    }
    
    @RequestMapping(method = RequestMethod.GET, value="/orders/cancel")
    String addOrder(@RequestParam("id") Long id) 
    {
        orderService.cancelOrder(id);
        return "redirect:/admin/orders";
    }
    
    @RequestMapping(value="/createroute")
    String createRoute(Model model) 
    {
        model.addAttribute("drivers", driverDao.getAllDrivers());
        model.addAttribute("today", LocalDate.now());
        return "createroute";
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/initroute")
    String newRoute(Model model, @RequestParam("driver_id") Long id, @RequestParam("date") @DateTimeFormat(iso=ISO.DATE) LocalDate date) 
    {
        Driver driver = driverDao.findById(id);
        boolean isCreated = routeService.createRoute(date, driver);
        if (isCreated)
            return "redirect:drivers/routes?id="+driver.getId();
        else
        {
            model.addAttribute("error", "К сожалению, в пункт производства, к которому привязан выбранный водитель, заказов на данную дату не поступалою");
            return createRoute(model);
        }
    }
}
//
//    FOR LIMITED NUMBER OF ORDERS PER DRIVER
//    
//    @RequestMapping(value="/createroute1")
//    String createroute1(Model model) 
//    {
//        model.addAttribute("drivers", driverDao.getAllDrivers());
//        model.addAttribute("today", LocalDate.now());
//        return "createroute1";
//    }
//    
//    @RequestMapping(method = RequestMethod.POST, value="/initroute1")
//    String initroute1(Model model, @RequestParam("driver_id") Long id, @RequestParam("count") int count, @RequestParam("date") @DateTimeFormat(iso=ISO.DATE) LocalDate date) 
//    {
//        Driver driver = driverDao.findById(id);
//        List<Order> orders = orderDao.findUntakenOrdersByDateForDriver(date, driver);
//        if (orders.size() < count)
//        {
//            model.addAttribute("error", "К сожалению, в пункт производства, к которому привязан выбранный водитель, заказов на данную дату не поступалою");
//            return createroute(model);
//        }
//        orders = orders.subList(0, count);
//        List<Order> ordersAsRoute = Solver.getOrdersAsRouteTEST(driver.getStock(), orders);
//        Route route = new Route(driver, date, ordersAsRoute);
//        if (routeDao.saveRoute(route))
//            log.info("New route created for " + route.getDriver().getUsername() + " with id=" + route.getId());
//        return "redirect:drivers/routes?id="+route.getDriver().getId();
//        //String[] split = lengths.split(",");
//        //ArrayList<ArrayList<Integer>> a = new ArrayList<>();
//        //return "redirect:/route";
//    }  
//    
//    @RequestMapping(value="/fill")
//    String fill() 
//    {
////        Driver driver = driverDao.findById(id);
////        List<Order> orders = orderDao.findUntakenOrdersByDateForDriver(date, driver);
////        if (orders.isEmpty())
////        {
////            model.addAttribute("error", "К сожалению, в пункт производства, к которому привязан выбранный водитель, заказов на данную дату не поступалою");
////            return createroute(model);
////        }
////        String places = Solver.generateRouteForGettingLengths(driver.getStock(), orders);
////        Route furuteRoute = new Route(driver, LocalDate.now(), orders);
////        model.addAttribute("places", places);
////        model.addAttribute(furuteRoute);
//        int lineCount=0;
//        if (places.isEmpty())
//        {
//            try (
//                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/polzovatel/Documents/ufa_addresses.csv"), "utf-8"));
//            ){
//                String s;
//                while ((s=br.readLine()) !=null ) {
//                    lineCount++;
//                    log.debug("input string: "+s);
//                    String[] split = s.split(";");
//                    if (split.length != 3)
//                        continue;
//                    
//                    Place p = new Place(split[2], new LatLng(Double.valueOf(split[0]),Double.valueOf(split[1])));
//                    //placeDao.save(p);
//                    places.add(p);
//                    //System.out.println(lineCount);
//                    
//                }
//            }
//            catch (IOException ex) {
//                System.out.println("Reading error in line "+lineCount);
//                ex.printStackTrace();
//            }
//        }
//        int i = 0;
//        Iterator<Place> it = places.iterator();
//        if (stockDao.getAllStocks().isEmpty())
//        {
//            while (it.hasNext())
//            {
//                Place p = it.next();
//                placeDao.save(p);
//                Stock stock = stockDao.addNewStock(p);
//                Driver dr = new Driver();
//                dr.setUsername("driver" + (i+1));
//                dr.setFullName("driver" + (i+1));
//                dr.setPassword("1234");
//                dr.setConfirmPassword("1234");
//                dr.setStock(stock);
//                driverService.save(dr);
//                it.remove();
//                i++;
//                if (i == 1)
//                    break;
//            }
//        }
//        Random rand = new Random();
//        int iter =0;
//        while (it.hasNext())
//        {
//            log.debug("Iteration: "+(++iter));
//            Place p = it.next();
//            LocalDate date = LocalDate.now();
//            try {
//                placeDao.save(p);
//            } catch (Exception ex)
//            {
//                continue;
//            }
//            orderDao.addNewOrder(p, date, "-");
//            if (iter % 55 == 0)
//                break;
//        }
//        return "redirect:createroute";
//    }
   
