/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.ga;

import com.tsp.model.LatLng;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author Linar Abzaltdinov
 */
public class GA
{
    Logger log = LoggerFactory.getLogger(GA.class);
    
    public static  double MUTATION_RATE = 0.3;
    public static  int POPILATION_SIZE = 30;
    public static  int NUM_OF_GENERATIONS = 1000;
    public static  int NUM_OF_CROSSOVER_GENES = 2;
    public static  int MUTATION_TYPE = 3; //1-reverse, 2-move, 3-reverse+move
    public int iterations = 0;
    Population population;
    ArrayList<ArrayList<Double>> dist;
    Random rand = new Random();
    int lastPopulationHashCode=0;
    int countOfRowEqualPopulations=0;
    
    public GA(ArrayList<ArrayList<Double>> dist, List<LatLng> locations)
    {
        this.dist = dist;
        population = new Population(dist, locations);
        lastPopulationHashCode = -1;
        countOfRowEqualPopulations = 1;
    }
    
    public List<Integer> run()
    {
        int generationIndex = 0;
        for (; generationIndex < NUM_OF_GENERATIONS; ++generationIndex)
        //while(true)
        {
            //System.out.println(generationIndex+" population:");
            //System.out.println(population.toString());
            evolve(population);
            //population.sortRoutesByFitness();
            //log.debug((generationIndex+1) + " generation:\n" + population.toString());
            if (population.hashCode() == lastPopulationHashCode)
            {
                countOfRowEqualPopulations++;
                if (countOfRowEqualPopulations >= GA.NUM_OF_GENERATIONS/100)
                {
                    break;
                }
            }
            else
            {
                lastPopulationHashCode = population.hashCode();
                countOfRowEqualPopulations = 1;
            }
        }
        log.info((generationIndex) + "generations \nGA returned genom: "+ population.getGenoms().last().getRoute());
        System.err.println("DIST: " + population.getGenoms().last().calculateTotalDistance());
        iterations = generationIndex;
        return population.getGenoms().last().getRoute();
    }
    
    public Population evolve (Population p)
    {
        return mutatePopulation(crossoverPopulation(p));
    }
    
    public Population mutatePopulation(Population p)
    {
        Genom last = p.getGenoms().last();
        for (Genom g : p.getGenoms().tailSet(last, false))
        {
            if (Math.random() < MUTATION_RATE)
            {
//                if (MUTATION_TYPE == 1) 
//                    partialReverseMutate(g); 
//                if (MUTATION_TYPE == 2) 
//                    moveMutate(g);
//                if (MUTATION_TYPE == 3)
//                {
//                    partialReverseMutate(g); 
//                    moveMutate(g);
//                }
            }
            fullReverseMutate(g);
        }
        return p;
    }
    
    public Population crossoverPopulation(Population p)
    {
        TreeSet<Genom> newGenoms = new TreeSet<Genom>();
        newGenoms.add(p.getGenoms().last());
        Iterator<Genom> it1 = p.getGenoms().iterator();
        while (it1.hasNext())
        {
            Genom g1 = it1.next();
            Iterator<Genom> it2 = p.getGenoms().tailSet(g1, false).iterator();
            while (it2.hasNext())
            {
                Genom g2 = it2.next();
                if (NUM_OF_CROSSOVER_GENES == 2)
                    newGenoms.add(EdgeRecombinationCrossover(g1, g2));
                if (NUM_OF_CROSSOVER_GENES == 3)
                {
                    Iterator<Genom> it3 = p.getGenoms().tailSet(g2, false).iterator();
                    while (it3.hasNext())
                    {
                        Genom g3 = it3.next();
                        if (it3.hasNext())
                            g3 = it3.next();
                        newGenoms.add(EdgeRecombinationCrossover(g1, g2, g3));
                    }
                }
            }
        }
        Iterator<Genom> d_it = newGenoms.descendingIterator();
        int count = 0;
        List<Genom> nextPopulationGenoms = new ArrayList<>(GA.POPILATION_SIZE);
        while (d_it.hasNext() && count++ < GA.POPILATION_SIZE)
            nextPopulationGenoms.add(d_it.next());
        p.setGenoms(nextPopulationGenoms);
        return p;
    }
    
    public Genom EdgeRecombinationCrossover(Genom g1, Genom g2)
    {
        ArrayList<Genom> genoms = new ArrayList<>();
        genoms.add(g1);
        genoms.add(g2);
        Collections.shuffle(genoms);
        return EdgeRecombinationCrossover(genoms);
    }
    
    public Genom EdgeRecombinationCrossover(Genom g1, Genom g2, Genom g3)
    {
        ArrayList<Genom> genoms = new ArrayList<>();
        genoms.add(g1);
        genoms.add(g2);
        genoms.add(g3);
        Collections.shuffle(genoms);
        return EdgeRecombinationCrossover(genoms);
    }
    
    public  Genom EdgeRecombinationCrossover(List<Genom> genoms)
    {
        //Random rand = new Random();
        int countOfPlaces = genoms.get(0).getSize()-1;  // -1 because it has place='0' twice
        if (genoms.get(1).getSize()-1 != countOfPlaces)
        {
            System.out.println("GENOMS HAS DIFFERENT SIZES: "+genoms.get(0).toString() +"; "+ genoms.get(1).toString());
//            log.warn("GENOMS HAS DIFFERENT SIZES: "+genoms.get(0).toString() +"; "+ genoms.get(1).toString());
        }
        List<List<Integer>> edges = new ArrayList<>(countOfPlaces); // 
        for (int i = 0; i < countOfPlaces; ++i)
        {
            edges.add(new LinkedList<Integer>());
        }
        for (int i = 0; i < countOfPlaces; ++i)
        {
            for (Genom g : genoms)
            {
                int first = g.getRoute().get(i);
                int second = g.getRoute().get(i+1);
                if (!edges.get(first).contains(second))
                    edges.get(first).add(second);
                double delta = Math.abs(dist.get(first).get(second)-dist.get(second).get(first)); //относительное отличие в расстояниях в прямую и обратную сторону
                if (delta < 0.03 && !edges.get(second).contains(first)) // если отличие менее 3%, то считаем, что в обратную сторону тоже есть дуга
                //if (!edges.get(second).contains(first))
                    edges.get(second).add(first);
            }
        }
        LinkedList<Integer> res = new LinkedList<>();
        int N = 0;
        res.add(N);
        while (res.size() < countOfPlaces)
        {
            for (List<Integer> e : edges)
            {
                int tmp = N;
                e.removeIf(x -> x == tmp);
            }
            if (!edges.get(N).isEmpty())
            {
                int min = Integer.MAX_VALUE;
                int ind = 0;
                for (int next : edges.get(N))
                {
                    if (edges.get(next).size() < min)
                    {
                        min = edges.get(next).size();
                        ind = next;
                    }
                }
                N = ind;
            }
            else
            {
                LinkedList<Integer> l1 = new LinkedList<>();
                for (int i = 0; i < countOfPlaces; ++i)
                    l1.add(i);
                l1.removeAll(res);
                N = l1.get(rand.nextInt(l1.size()));
            }
            res.add(N);
        }
        res.add(0);
        return new Genom(res, dist);
    }
    
    public void fullReverseMutate(Genom g)
    {
        double baseFitness = g.getFitness();
        ArrayList<Integer> reversedRoute = new ArrayList<>(g.getRoute());
        Collections.reverse(reversedRoute);
        Genom g1 = new Genom(reversedRoute, dist);
        if (g1.getFitness() > baseFitness)
            g = g1;
    }
    
    public void partialReverseMutate(Genom g)
    {
        int n = g.getSize();
        int k1 = rand.nextInt(g.getSize());
        int l = rand.nextInt(n-k1);
        List<Integer> sourceRoute = g.getRoute();
        List<Integer> newRoute = new ArrayList<>();
        int i = 0;
        for (i = 0; i < k1; ++i)
            newRoute.add(sourceRoute.get(i));
        for (i = k1+l - 1; i >= k1; --i)
            newRoute.add(sourceRoute.get(i));
        for (i = k1+l; i < n; ++i)
            newRoute.add(sourceRoute.get(i));
        if (newRoute.size() != sourceRoute.size())
            log.warn("BUG IN REVERSE - size changed");
        g.setRoute(newRoute);
    }
    /**
     * Перемещает часть гена от k1 до k2 аллели на m позиций вперед(m>0) или назад (m < 0)
     * @param g Genom to mutate
     */
    public void moveMutate(Genom g) 
    {
        int n = g.getSize();
        int k1 = rand.nextInt(g.getSize());
        int k2 = rand.nextInt(g.getSize());
        if (k2 < k1)
        {
            int tmp = k1;
            k1=k2;
            k2 = tmp;
        }
        int l = k2-k1+1;
        int m = rand.nextInt(n-l+1);
        m-=k1;
        //int new_k1 = (k1+m)%(n-l-1);
        //m = new_k1 - k1;
        List<Integer> sourceRoute = g.getRoute();
        List<Integer> newRoute = new ArrayList<>();
        System.out.println("m="+m+", k1="+k1+", k2="+k2);
        if (m == 0)
            return;
        if (m > 0)
        {
            for (int i = 0; i < k1; ++i)
                newRoute.add(sourceRoute.get(i));
            for (int i = k2+1; i < k2+m+1; ++i)
                newRoute.add(sourceRoute.get(i));
            for (int i = k1; i <= k2; ++i)
                newRoute.add(sourceRoute.get(i));
            for (int i = k2+m+1; i < n; ++i)
                newRoute.add(sourceRoute.get(i));
        } else {
            for (int i = 0; i < k1-(-m); ++i)
                newRoute.add(sourceRoute.get(i));
            for (int i = k1; i <= k2; ++i)
                newRoute.add(sourceRoute.get(i));
            for (int i = k1-(-m); i < k1; ++i)
                newRoute.add(sourceRoute.get(i));
            for (int i = k2+1; i < n; ++i)
                newRoute.add(sourceRoute.get(i));
        }
        if (newRoute.size() != sourceRoute.size())
            log.warn("BUG IN MOVE - size changed");
        g.setRoute(newRoute);
    }
    
}
