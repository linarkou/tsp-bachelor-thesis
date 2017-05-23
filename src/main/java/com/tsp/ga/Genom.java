/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.ga;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Содeржит порядок обхода точек начиная от 0 и заканчивая в 0 (гамильтонов цикл) - НЕТ
 * порядок обхода точек 1..n
 * 
 * @author Linar Abzaltdinov
 */
public class Genom implements Comparable
{
    /**
     * Расстояния между точками
     */
    private ArrayList<ArrayList<Double>> dist; 
    
    private List<Integer> locations = new ArrayList<>();
    private boolean isFitnessChanged = true;
    private double fitness = 0;
    public Genom(Collection<Integer> r, ArrayList<ArrayList<Double>> d)
    {
        locations.addAll(r);
        dist = d;
    }
    
    public double getFitness()
    {
        if (isFitnessChanged)
        {
            fitness = 1/calculateTotalDistance()*100000;
            isFitnessChanged = false;
        }
        return fitness;
    }
    
    public double calculateTotalDistance()
    {
        double totalDist = 0;
        int size = locations.size();
        for (int i = 0; i < size; ++i)
            totalDist+=dist.get(locations.get(i)).get(locations.get((i+1)%size));
        return totalDist;
    }
    
//    public double calculateTotalDistance()
//    {
//        double totalDist = dist.get(0).get(locations.get(0));
//        int size = locations.size();
//        for (int i = 0; i < size-1; ++i)
//            totalDist+=dist.get(locations.get(i)).get(locations.get(i+1));
//        totalDist += dist.get(locations.get(size-1)).get(0);
//        return totalDist;
//    }

    /**
     * @return the locations
     */
    public List<Integer> getRoute()
    {
        isFitnessChanged = true;
        return locations;
    }

    /**
     * @param route the locations to set
     */
    public void setRoute(List<Integer> route)
    {
        isFitnessChanged = true;
        this.locations.clear();
        locations.addAll(route);
    }
    
    public int getSize()
    {
        return locations.size();
    }
    
    @Override
    public String toString()
    {
        return locations.toString()+ "; Fitness = " + getFitness() + "; Distance = " + calculateTotalDistance();
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Genom))
            return false;
        Genom g = (Genom) o;
        if (g.getSize() != this.getSize())
            return false;
        for (int i = 0; i < locations.size(); ++i)
            if (!locations.get(i).equals(g.getRoute().get(i)))
                return false;
        return true;
    }
    
    @Override 
    public int hashCode()
    {
        return Double.hashCode(getFitness());
    }

    @Override
    public int compareTo(Object object)
    {
        if (object == null) 
        {
            throw new NullPointerException("Null parameter");
        } 
        else 
        {
            if (!this.getClass().equals(object.getClass())) 
                throw new ClassCastException("Possible ClassLoader issue.");
            else 
                return Double.compare(this.getFitness(), ((Genom)object).getFitness());
        }
    }
}
