/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.converter;

import com.tsp.dao.StockDao;
import com.tsp.model.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
/**
 *
 * @author polzovatel
 */
public class StringToStock implements Converter<String, Stock> {
    @Autowired
    StockDao stockDao;
    
    public Stock convert(String id) {
        return stockDao.findStockById(Long.valueOf(id));
    }
} 