package com.tsp.ga;

import com.tsp.model.LatLng;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.IntStream;

public class Population
{
    private TreeSet<Genom> routes = new TreeSet<>();
    
    public Population(ArrayList<ArrayList<Double>> dist, List<LatLng> locations)
    {
        Genom g1 = buildRouteAsRound(dist, locations);
        Genom g2 = buildRouteByDistanceFromStart(dist);
        Genom g3 = buildRouteByDistanceFromLastAdded(dist);
        routes.add(g1);
        routes.add(g2);
        routes.add(g3);
        for (int i = 3; i < GA.POPILATION_SIZE; ++i)
        {
            routes.add(buildRandomRoute(dist));
        }
        //sortRoutesByFitness();
    }
    
    public Population(Collection<Genom> genoms)
    {
        if (genoms.size() <= GA.POPILATION_SIZE)
        {
            routes.addAll(genoms);
            //sortRoutesByFitness();
        }
    }
    
    
    /**
     * @return the routes
     */
    public TreeSet<Genom> getGenoms()
    {
        return routes;
    }
    
    public void setGenoms(Collection<Genom> genoms)
    {
        if (genoms.size() <= GA.POPILATION_SIZE)
        {
            routes.clear();
            routes.addAll(genoms);
            //sortRoutesByFitness();
        } 
    }
    
    /*public void sortRoutesByFitness()
    {
        routes.sort((g1, g2) -> -Double.compare(g1.getFitness(), g2.getFitness()));
    }*/
    
    private Genom buildRouteAsRound(ArrayList<ArrayList<Double>> dist, List<LatLng> locations)
    {
        class Point implements Comparable
        {
            Point(double _x, double _y) { x=_x; y=_y;};
            double x;
            double y;

            @Override
            public int compareTo(Object o) //
            {
                if (!(o instanceof Point))
                {
                    throw new ClassCastException(o.toString() + " is not a " + Point.class);
                }
                Point p = (Point) o;
                if (p.y*y < 0)
                {
                    return y > p.y ? -1 : 1;
                } 
                else 
                {
                    if (p.x*x < 0)
                    {
                        if (y > 0)
                            return x > p.x ? -1 : 1;
                        else
                            return x < p.x ? -1 : 1;
                    } 
                    else 
                    {
                        double k1 = y/x;
                        double k2 = p.y/p.x;
                        return (k2-k1)/(1+k1*k2) > 0 ? -1 : 1;
                    }
                }
            }
        }
        LinkedList<Integer> places = new LinkedList<>();
        TreeMap<Point, Integer> points = new TreeMap<>();
        double lng0 = locations.get(0).getLng();
        double lat0 = locations.get(0).getLat();
        for (int i = 1; i < locations.size(); ++i)
        {
            Point e = new Point(locations.get(i).getLng()-lng0, locations.get(i).getLat()-lat0);
            points.put(e, i);
        }
        places.add(0);
        while (!points.isEmpty())
            places.add(points.pollFirstEntry().getValue());
        places.add(0);
        return new Genom(places, dist);
    }
    
    private Genom buildRouteByDistanceFromStart(ArrayList<ArrayList<Double>> dist)
    {
        LinkedList<Integer> places = new LinkedList<>();
        TreeMap<Double, Integer> distFromStart = new TreeMap<>();
        for (int i = 1; i < dist.size(); ++i)
            distFromStart.put(dist.get(0).get(i), i);
        places.add(0);
        while (!distFromStart.isEmpty())
            places.add(distFromStart.pollFirstEntry().getValue());
        places.add(0);
        return new Genom(places, dist);
    }
    
    private Genom buildRouteByDistanceFromLastAdded(ArrayList<ArrayList<Double>> dist)
    {
        LinkedList<Integer> places = new LinkedList<>();
        HashSet<Integer> used = new HashSet<>();
        int last = 0;
        places.add(last);
        used.add(last);
        int size = dist.size();
        while (used.size() < size)
        {
            double min = Double.MAX_VALUE;
            int ind = -1;
            for (int i = 0; i < size; ++i)
            {
                if (!used.contains(i) && dist.get(last).get(i) < min)
                {
                    min = dist.get(last).get(i);
                    ind = i;
                }
            }
            last = ind;
            places.add(last);
            used.add(last);
        }
        places.add(0);
        return new Genom(places, dist);
    }
    
    public Genom buildRandomRoute(ArrayList<ArrayList<Double>> dist)
    {
        LinkedList<Integer> places = new LinkedList<>();
        IntStream.range(1,dist.size()).forEach(x -> places.add(x));
        Collections.shuffle(places);
        places.add(0,0);
        places.add(0);
        return new Genom(places, dist);
    }
    
    public int getSize()
    {
        return routes.size();
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (Genom g : routes)
        {
            sb.append(g).append("\n");
        }
        return sb.toString();
    }
    
    @Override
    public int hashCode()
    {
        return (int)((this.routes.first().getFitness()+this.routes.last().getFitness())*10000);
    }
}
