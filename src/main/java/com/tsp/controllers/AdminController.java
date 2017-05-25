/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.controllers;

import com.tsp.dao.ClientDao;
import com.tsp.dao.DriverDao;
import com.tsp.dao.OrderDao;
import com.tsp.dao.StockDao;
import com.tsp.model.Client;
import com.tsp.model.Driver;
import com.tsp.model.Order;
import com.tsp.model.Stock;
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
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Linar Abzaltdinov
 */
@Controller
@RequestMapping(value = "/admin")
@RolesAllowed(value = "ROLE_ADMIN")
public class AdminController
{
    @Autowired
    ClientDao clientDao;
    
    @Autowired
    StockDao stockDao;
    
    @Autowired
    DriverDao driverDao;
    
    @Autowired
    OrderDao orderDao;
    
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
        List<Order> orders = orderDao.findOrdersByClientId(id);
        ModelAndView mv = new ModelAndView("orders");
        mv.addObject("orders", orders);
        mv.addObject("username", clientDao.findById(id).getUsername());
        return mv;
    }
    
    @RequestMapping(value="/orders")
    ModelAndView orders() 
    {
        List<Order> allOrders = orderDao.findAllOrders();
        ModelAndView mv = new ModelAndView("orders");
        mv.addObject("orders", allOrders);
        mv.addObject("today", LocalDate.now());
        return mv;
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/orders/add")
    String addOrder(@RequestParam("address") String address, @RequestParam("desc") String description, @RequestParam("date") @DateTimeFormat(iso=ISO.DATE) LocalDate date) 
    {
        orderDao.addNewOrder(address, date, description);
        return "redirect:/admin/orders";
    }
    
    @RequestMapping(method = RequestMethod.GET, value="/orders/cancel")
    String addOrder(@RequestParam("id") Long id) 
    {
        orderDao.cancelOrder(id);
        return "redirect:/admin/orders";
    }
    
}
