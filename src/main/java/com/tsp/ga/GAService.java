/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.ga;

import com.tsp.dao.ClientDao;
import com.tsp.model.Client;
import com.tsp.model.LatLng;
import com.tsp.model.Order;
import com.tsp.model.Place;
import com.tsp.model.Route;
import com.tsp.model.Stock;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Linar Abzaltdinov
 * @version 1.0
 */
public class GAService
{
    static Logger log = LoggerFactory.getLogger(GAService.class);

    /**
     * Возвращает строку-путь, который проходит между каждыми двумя точками
     * 
     * Например, для точек a,b,c строится в виде: |'a','b','a','c','a',|'b','c','b'.
     * Результат используется для вычисления длин путей от каждого клиента к каждому (и к складу) в YMAPS API.
     * 
     * @param places
     * @return String 
     */
    public static String generateRouteForGettingLengths(Stock stock, List<Order> orders)
    {
        StringBuilder sb = new StringBuilder("\'");
        String separator = "\', \'";
        List<Place> places = new ArrayList<Place>();
        places.add(stock.getPlace());
        places.addAll(orders.stream().map(x -> x.getPlace()).collect(Collectors.toList()));
        int n = places.size();
        for (int i = 0; i < n-1; ++i) 
        {
            for (int j = i+1; j < n; ++j)
            {
                sb.append(places.get(i).getAddress());
                sb.append(separator);
                sb.append(places.get(j).getAddress());
                sb.append(separator);
            }
            sb.append(places.get(i).getAddress());
            sb.append(i != n-2 ? separator : "\'");
        }
        return sb.toString();
    }
    
    
    /**
     * 
     * По списку клиентов и длинам между ними находит кратчайший маршрут
     * 
     * @param stock Склад, откуда развозить товары
     * @param orders Список заказов
     * @param lengths Длина путей
     * @return 
     */
    public static List<Order> getOrdersAsRoute(Stock stock, List<Order> orders, String lengths)
    {
        List<Place> sourcePlaces = new ArrayList<Place>();
        sourcePlaces.add(stock.getPlace());
        sourcePlaces.addAll(orders.stream().map(x -> x.getPlace()).collect(Collectors.toList()));
        ArrayList<ArrayList<Double>> dist = parseLengths(lengths, sourcePlaces.size());
        ArrayList<LatLng> locations = new ArrayList<LatLng>();
        locations.add(stock.getPlace().getLocation());
        for (Order or : orders)
            locations.add(or.getPlace().getLocation());
        GA ga = new GA(dist, locations);
        List<Integer> order = ga.run(); //итоговый порядок прохода, начинается с 0 и заканачивается в 0
        List<Order> resultedRoute = new ArrayList<>(sourcePlaces.size());
        log.debug(" RESULTED ORDER:" + order.toString());
        for (int i = 1; i < order.size()-1; ++i)
        {
            resultedRoute.add(orders.get(order.get(i)-1));
        }
        return resultedRoute;
    }
    
    public static ArrayList<ArrayList<Double>> parseLengths(String lengths, int n)
    {
        ArrayList<ArrayList<Double>> l = new ArrayList<ArrayList<Double>>(n);
        for (int i = 0; i < n; ++i)
        {
            l.add(new ArrayList<>(Collections.nCopies(n, 0d)));
        }
        String[] split = lengths.split(",");
        int k = 0;
        for (int i = 0; i < n-1; ++i)
        {
            for (int j = i+1; j < n; ++j)
            {
                if (k+1 < split.length)
                {
                    Double ij = Double.valueOf(split[k].trim());
                    Double ji = Double.valueOf(split[k+1].trim());
                    l.get(i).set(j, ij);
                    l.get(j).set(i, ji);
                    k+=2;
                }
            }
            k++;
        }
        return l;
    }
}
