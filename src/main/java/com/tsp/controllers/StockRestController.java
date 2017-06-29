/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.controllers;

import com.google.gson.Gson;
import com.tsp.dao.StockDao;
import com.tsp.model.Place;
import com.tsp.model.Stock;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Linar Abzaltdinov
 * @version 1.0
 */
@RestController
@RolesAllowed(value = "ROLE_ADMIN")
public class StockRestController
{
    private Logger log = LoggerFactory.getLogger(StockRestController.class);
    
    @Autowired
    StockDao stockDao;
    
    @RequestMapping(method = RequestMethod.POST, value="/admin/stocks/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Stock addStock(@RequestParam("address") String address) 
    {
        Stock newStock = stockDao.addNewStock(address);
        log.info("Added stock id="+newStock.getId()+" & place="+newStock.getPlace());
        return newStock;
    }
    
    @RequestMapping(method = RequestMethod.DELETE, value="/admin/stocks/{id}")
    public String removeStock(@PathVariable("id") Long id) 
    {
        stockDao.removeStockById(id);
        log.info("Removed stock id="+id);
        return "successfully deleted";
    }
    
    @RequestMapping(method = RequestMethod.GET, value="/admin/stocks/{id}")
    public StockInfo getStock(@PathVariable("id") Long id) 
    {
        return new StockInfo(stockDao.findStockById(id));
    }
    
    @RequestMapping(method = RequestMethod.GET, value="/admin/stocks/all")
    public List<Stock> getAllStocks() 
    {
        return stockDao.getAllStocks();
    }

    private static class StockInfo
    {

        private Long id;
        private Place place;

        private StockInfo(Stock s)
        {
            this.id = s.getId();
            this.place = s.getPlace();
        }

        /**
         * @return the id
         */
        public Long getId()
        {
            return id;
        }

        /**
         * @return the place
         */
        public Place getPlace()
        {
            return place;
        }
    }
}
