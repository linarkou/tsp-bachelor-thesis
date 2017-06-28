/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.solver;

import com.google.gson.*;
import com.tsp.dao.ClientDao;
import com.tsp.ga.GA;
import com.tsp.geocode.Geocoder;
import com.tsp.model.Client;
import com.tsp.model.LatLng;
import com.tsp.model.Order;
import com.tsp.model.Place;
import com.tsp.model.Route;
import com.tsp.model.Stock;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 *
 * @author Linar Abzaltdinov
 * @version 1.0
 */
public class Solver
{
    static Logger log = LoggerFactory.getLogger(Solver.class);

    public static ArrayList<ArrayList<Double>> getDistancesFromGoogle(List<LatLng> places)
    {
        List<String> addresses = places.stream().map(x -> x.toString()).collect(Collectors.toList());
        ArrayList<ArrayList<Double>> result = new ArrayList<>(places.size());
        for (int i = 0; i < places.size(); ++i)
        {
            result.add(new ArrayList<Double>(places.size()));
            for (int j = 0; j < places.size(); ++j)
            {
                result.get(i).add(0.0);
            }
        }
        String url_start = "https://maps.googleapis.com/maps/api/distancematrix/json?";
        String origins = "origins=";
        String destinations= "&destinations=";
        String key = "&key=AIzaSyDuEwRXx89RTym5nCtjGmRzsrp4JZV_zS4";
        int count = places.size();
        for (int i = 0; i <= places.size()/25; ++i)
        {
            int n = Math.min(count,25);
            String orig = new String(origins);
            for (int j = i*25; j < i*25+n; ++j)
            {
                if (j != i*25)
                    orig+="|";
                orig+=addresses.get(j);
            }
            int max_places_for_one_send = 100 / n;
            int count2 = places.size();
            for (int k = 0; k <= places.size() / max_places_for_one_send; ++k)
            {
                int m = Math.min(max_places_for_one_send, count2);
                String dest = new String(destinations);
                for (int j=k*max_places_for_one_send; j < k*max_places_for_one_send+m ; ++j)
                {
                    if (j != k*max_places_for_one_send)
                        dest+="|";
                    dest+=addresses.get(j);
                }
                count2-=m;
                String url = url_start+orig+dest+key;
                log.debug("URL="+url);
                try
                {
                    Thread.sleep(100);
                    InputStreamReader in = new InputStreamReader((new URL(url)).openStream());
                    JsonElement jelement = new JsonParser().parse(in);
                    JsonObject jobject = jelement.getAsJsonObject();
                    JsonArray rows = jobject.getAsJsonArray("rows");
                    int ii = i*25;
                    for (JsonElement je : rows)
                    {
                        JsonArray elements = je.getAsJsonObject().getAsJsonArray("elements");
                        int jj = k*max_places_for_one_send;
                        for (JsonElement je2 : elements)
                        {
                            long value = je2.getAsJsonObject().getAsJsonObject("distance").get("value").getAsLong();
                            result.get(ii).set(jj, value*1.0);
                            jj++;
                        }
                        ii++;
                    }
                } catch (Exception ex)
                {
                    java.util.logging.Logger.getLogger(Solver.class.getName()).log(Level.SEVERE, "Unknown exception :D", ex);
                }
            }
            count -= n;
            if (count == 0)
                break;
        }
        return result;
    }
    
    public static ArrayList<ArrayList<Double>> getDistancesFromMapBox(List<LatLng> places)
    {
        List<String> addresses = places.stream().map(x -> x.toLngLat()).collect(Collectors.toList());
        ArrayList<ArrayList<Double>> result = new ArrayList<>(places.size());
        for (int i = 0; i < places.size(); ++i)
        {
            result.add(new ArrayList<Double>(places.size()));
            for (int j = 0; j < places.size(); ++j)
            {
                result.get(i).add(0.0);
            }
        }
        String url_start = "https://api.mapbox.com/directions-matrix/v1/mapbox/driving/";
        String destinations= "&destinations=";
        String sources = "?sources=";
        String key = "access_token=pk.eyJ1IjoibGluYXI5NSIsImEiOiJjajNyOHFreXcwMDBwNHVvMmIwdDA3ZTdpIn0.nnpzy7elxJr2cM-gPKaAZQ";
        int count = places.size();
        if (count <= 25)
        {
            String coords = "";
            for (int i = 0; i < count; ++i)
            {
                if (i != 0)
                {
                    coords += ";";
                }
                coords += addresses.get(i);
            }
            String url = url_start + coords + "?" + key;
            log.debug("URL=" + url);
            try
            {
                //Thread.sleep(100);
                InputStreamReader in = new InputStreamReader((new URL(url)).openStream());
                JsonElement jelement = new JsonParser().parse(in);
                JsonObject jobject = jelement.getAsJsonObject();
                JsonArray rows = jobject.getAsJsonArray("durations");
                int i = 0;
                for (JsonElement je : rows)
                {
                    JsonArray elements = je.getAsJsonArray();
                    int j = 0;
                    for (JsonElement je2 : elements)
                    {
                        double value = je2.getAsDouble();
                        result.get(i).set(j, value * 1.0);
                        j++;
                    }
                    i++;
                }
            } catch (Exception ex)
            {
                java.util.logging.Logger.getLogger(Solver.class.getName()).log(Level.SEVERE, "Unknown exception :D", ex);
            }
            return result;
        }
        //if count > 25
        int url_count = 0;
        int maxSize = 12; //max size of Matrix for one request 12*12
        for (int i = 0; i <= places.size()/maxSize; ++i)
        {
            int n = Math.min(count,maxSize);
            String coords1 = "";
            String src = new String(sources);
            for (int j = i*maxSize; j < i*maxSize+n; ++j)
            {
                if (j != i*maxSize)
                {
                    coords1+=";";
                    src+=";";
                }
                coords1+=addresses.get(j);
                src+=j-i*maxSize;
            }
            int count2 = places.size();
            for (int k = 0; k <= places.size() / maxSize; ++k)
            {
                int m = Math.min(maxSize, count2);
                String coords2 = "";
                String dst = new String(destinations);
                for (int j=k*maxSize; j < k*maxSize+m ; ++j)
                {
                    if (j != k*maxSize)
                    {
                        dst+=";";
                    }
                    dst+=j-k*maxSize;
                    coords2+=";";
                    coords2+=addresses.get(j);
                }
                count2-=m;
                String url = url_start+coords1+coords2+src+dst+"&"+key;
                log.debug("URL="+url);
                try
                {
                    if (url_count == 60)
                    {
                        Thread.sleep(60000);
                        url_count = 0;
                    }
                    InputStreamReader in = new InputStreamReader((new URL(url)).openStream());
                    url_count++;
                    JsonElement jelement = new JsonParser().parse(in);
                    JsonObject jobject = jelement.getAsJsonObject();
                    JsonArray rows = jobject.getAsJsonArray("durations");
                    int ii = i*maxSize;
                    for (JsonElement je : rows)
                    {
                        JsonArray elements = je.getAsJsonArray();
                        int jj = k*maxSize;
                        for (JsonElement je2 : elements)
                        {
                            double value = je2.getAsDouble();
                            result.get(ii).set(jj, value*1.0);
                            jj++;
                        }
                        ii++;
                    }
                } catch (Exception ex)
                {
                    java.util.logging.Logger.getLogger(Solver.class.getName()).log(Level.SEVERE, "Unknown exception :D", ex);
                }
            }
            count -= n;
            if (count == 0)
                break;
        }
        return result;
    }
    
    /**
     * 
     * По списку клиентов и длинам между ними находит кратчайший маршрут
     * 
     * @param stock Склад, откуда развозить товары
     * @param orders Список заказов
     * @return 
     */
    public static List<Order> getOrdersAsRoute(Stock stock, List<Order> orders)
    {
        ArrayList<LatLng> allLocations = new ArrayList<LatLng>();
        allLocations.add(stock.getPlace().getLocation());
        for (Order or : orders)
            allLocations.add(or.getPlace().getLocation());
        ArrayList<ArrayList<Double>> dist = getDistancesFromMapBox(allLocations);
        ArrayList<LatLng> locations = new ArrayList<LatLng>();
        locations.add(stock.getPlace().getLocation());
        for (Order or : orders)
            locations.add(or.getPlace().getLocation());
        GA ga = new GA(dist, locations);
        List<Integer> order = ga.run();
        List<Order> resultedRoute = new ArrayList<>(allLocations.size());
        log.debug(" RESULTED ORDER:" + order.toString());
        for (int i = 1; i < order.size()-1; ++i)
        {
            resultedRoute.add(orders.get(order.get(i)-1));
        }
        return resultedRoute;
        
    }
    
//    public static List<Order> getOrdersAsRouteTEST(Stock stock, List<Order> orders)
//    {
//        log.debug("SOURCE ORDERS:");
//        for (Order o : orders)
//            log.debug(o.getPlace().toString());
//        
//        ArrayList<LatLng> allLocations = new ArrayList<LatLng>();
//        allLocations.add(stock.getPlace().getLocation());
//        for (Order or : orders)
//            allLocations.add(or.getPlace().getLocation());
//        
//        //ArrayList<ArrayList<Double>> dist = getDistancesFromGoogle(allLocations);
//        ArrayList<ArrayList<Double>> dist = getDistancesFromMapBox(allLocations);
//        log.info("DISTANCES: "+dist.toString());
//        GA ga;
//        List<Integer> order;
//        List<Integer> temp_res_order = null;
//        Double minDistance = Double.MAX_VALUE;
//        
//        try (BufferedWriter bw = new BufferedWriter(new FileWriter("/Users/polzovatel/Documents/experiment.csv", true))) {
//
//                List<LatLng> locations = allLocations;//.subList(0, k);
//                bw.write(locations.size() + " адресов");
//                log.debug(locations.size() + " адресов");
//                for (GA.MUTATION_RATE = 0.1; GA.MUTATION_RATE <= 0.5; GA.MUTATION_RATE+=0.2)
//                    for (GA.NUM_OF_CROSSOVER_GENES = 2; GA.NUM_OF_CROSSOVER_GENES <= 3; GA.NUM_OF_CROSSOVER_GENES++)
//                        for (GA.MUTATION_TYPE = 1; GA.MUTATION_TYPE <= 3; GA.MUTATION_TYPE++)
//                        {
//                            List<Long> times = new ArrayList<>(10);
//                            List<Double> dists = new ArrayList<>(10);
//                            long min_time = Long.MAX_VALUE;
//                            Double min_dist = Double.MAX_VALUE;
//                            for (int i = 0; i < 10/*(int)Math.round(Math.sqrt(GA.MUTATION_RATE*100))*/; ++i)
//                            {
//                                ga = new GA(dist, locations);
//                                long start = System.currentTimeMillis();
//                                order = ga.run(); //итоговый порядок прохода, начинается с 0 и заканачивается в 0
//                                long end = System.currentTimeMillis();
//                                Double curDistance = getDistance(order, dist);
//                                long time = end-start;
//                                times.add(time);
//                                dists.add(curDistance);
//                                if (time < min_time)
//                                    min_time = time;
//                                if (curDistance < min_dist)
//                                    min_dist = curDistance;
//                                if (curDistance < minDistance)
//                                {
//                                    minDistance = curDistance;
//                                    temp_res_order=order;
//                                }
//                            }
//                            Double avg_time = times.stream().mapToLong(Long::longValue).average().getAsDouble();
//                            Double time_otkl = Math.sqrt(times.stream().map(x -> (x-avg_time)*(x-avg_time)).mapToDouble(Double::doubleValue).average().getAsDouble());
//                            Double avg_dist = dists.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
//                            Double dist_otkl = Math.sqrt(dists.stream().map(x -> (x-avg_dist)*(x-avg_dist)).mapToDouble(Double::doubleValue).average().getAsDouble());
//                            String mutationType = GA.MUTATION_TYPE == 1 ? "reverse" : (GA.MUTATION_TYPE == 2 ? "move" : "reverse+move");
//                            bw.newLine();
//                            bw.write(min_time+","+avg_time+","+time_otkl+","+min_dist+","+avg_dist+","+dist_otkl+","+mutationType + ","+GA.NUM_OF_CROSSOVER_GENES+","+GA.MUTATION_RATE);
//                        
//                bw.newLine();
//            }
//            /*BB bb = new BB(dist, locations.size());
//            long start = System.currentTimeMillis();
//            order = bb.run();
//            long end = System.currentTimeMillis();
//            Double curDistance = getDistance(order, dist);
//            bw.write("BB,"+(end-start)+","+curDistance);
//            bw.newLine();
//            bw.newLine();
//            log.info("BB,"+(end-start)+","+curDistance, ", "+order.toString());
//            
//            BruteForce BF = new BruteForce(dist, locations.size() - 1);
//            long start = System.currentTimeMillis();
//            order = BF.run();
//            long end = System.currentTimeMillis();
//            Double curDistance = getDistance(order, dist);
//            bw.write("BF,"+(end-start)+","+curDistance);
//            bw.newLine();
//            bw.newLine();
//            log.info("BF,"+(end-start)+","+curDistance, ", "+order.toString());
//            */
//        } catch (IOException ex) 
//        {
//            java.util.logging.Logger.getLogger(Solver.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        List<Order> resultedRoute = new ArrayList<>(allLocations.size());
//        log.debug(" RESULTED ORDER:" + temp_res_order.toString());
//        for (int i = 0; i < temp_res_order.size(); ++i)
//        {
//            resultedRoute.add(orders.get(temp_res_order.get(i)-1));
//        }
//        /*log.debug(" NEW ORDERS:");
//        for (Order o : resultedRoute)
//            log.debug(o.getPlace().toString());
//        */
//        return resultedRoute;
//    }
    
    private static Double getDistance(List<Integer> order, ArrayList<ArrayList<Double>> dist)
    {
        Double res = 0.0;
        if (order.get(0)==0)
            order.remove(0);
        if (order.get(order.size()-1)==0)
            order.remove(order.size()-1);
        for (int i = 0; i < order.size()-1; ++i)
            res+=dist.get(order.get(i)).get(order.get(i+1));
        res+=dist.get(0).get(order.get(0));
        res+=dist.get(order.get(order.size()-1)).get(0);
        return res;
    }
}