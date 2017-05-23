package com.tsp.controllers;

import com.tsp.dao.ClientDao;
import com.tsp.dao.PlaceDao;
import com.tsp.dao.RouteDao;
import com.tsp.dao.StockDao;
import com.tsp.ga.GAService;
import com.tsp.model.Place;
import com.tsp.geocode.Geocoder;
import com.tsp.model.Client;
import com.tsp.model.LatLng;
import com.tsp.model.Route;
import com.tsp.model.Stock;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.security.RolesAllowed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {
    
    @RequestMapping(value = {"/", "/welcome"}, method = RequestMethod.GET)
    public String welcome(Model model) {
        
        return "welcome";
    }
    
    Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    ClientDao clientDao;
    
    @Autowired
    PlaceDao placeDao;
    
    @Autowired
    StockDao stockDao;
    
    @Autowired
    RouteDao routeDao;
    
    @RequestMapping("route")
    public ModelAndView route() 
    {
        ModelAndView mv = new ModelAndView("route");
        return mv;
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "route")
    public ModelAndView routeCalc(Model model, @RequestParam("lengths") String lengths) 
    {
        List<Client> allClients = clientDao.getAllClients();
//        Route route = GAService.getRoute(stock, allClients, lengths);
//        routeDao.saveRoute(route);
        ModelAndView mv = route();
//        mv.addObject("route", route.toString());
        return mv;
        //String[] split = lengths.split(",");
        //ArrayList<ArrayList<Integer>> a = new ArrayList<>();
        //return "redirect:/route";
    }
    
    @RequestMapping("/initroute")
    ModelAndView initroute() 
    {
        ModelAndView mv = new ModelAndView("initroute");
        /*String s1="\'Уфа Цюрупы 149\'";
        String s2="\'Уфа карла маркса 12\'";
        String s3="\'Уфа Ленина 20\'";
        String route = s1+", "+ s2 + ", " + s3;//s1+", "+ s3+", "+ s2+", "+s3+", "+s1;*/
        List<Client> allClients = clientDao.getAllClients();
        //String route = GAService.generateRouteForGettingLengths(stock,  );
        //mv.addObject("route", route);
        
        return mv;
    }
}
