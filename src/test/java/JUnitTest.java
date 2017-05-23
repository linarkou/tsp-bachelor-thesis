/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.tsp.ga.GA;
import com.tsp.ga.GAService;
import com.tsp.model.LatLng;
import com.tsp.model.Place;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @author polzovatel
 */
public class JUnitTest
{
    //@Test
    public void test1()
    {
       Logger log = LoggerFactory.getLogger(JUnitTest.class);
       String s = "5254.27,4961.35,13876.02,14513.33,6426.97,6969.07,7088.61,6415.16,15891.97,16222.42,15777.25,15965.24,3370.26,3098.31,7617.85,7310.01,2657.04,2906.64,5254.27,16593.65,16901.11,9977.75,8462.62,8115.7,7014.25,10919.69,10566.23,10804.97,10238.48,7185.18,7202.78,2652.61,2317.72,6435.37,7351.53,17466.89,12587.21,13264.43,14033.79,14196.13,26823.79,27084.64,26709.07,26827.46,14014.93,14176.26,19120.95,18979.07,12761.55,13544.72,12341.24,1786.11,1636.92,17719.75,19595.49,17508.37,19584.29,6470.67,6481.25,10167.01,9829.62,5217.3,5849.7,1786.11,16174.72,17005.1,16060,16068.63,7402.37,8212.33,8718.65,8384.45,6149,7580.78,16174.72,445.2,802.18,18762.2,18875.32,8635.28,8885.11,18012.39,19024.07,329.62,18505.02,18760.6,8378.1,8770.39,17755.21,18909.35,18505.02,9753.23,9533.85,2026.4,1319.73,9995.84,8784.04,9563.62";
       ArrayList<Place> locations = new ArrayList<>();
       locations.add(new Place(new LatLng(54.74287600,55.96054200))); //цюрупы 149
       locations.add(new Place(new LatLng(54.75627700,56.00993100))); //комсомольская 109
       locations.add(new Place(new LatLng(54.71213900,55.83435500))); //дагестанская 25
       locations.add(new Place(new LatLng(54.69847800,55.98569400))); //софьи перовской 42
       locations.add(new Place(new LatLng(54.70593900,55.99722900))); //степана кувыкина 25/1
       locations.add(new Place(new LatLng(54.81753600,56.11283300))); //кремлевская 57
       locations.add(new Place(new LatLng(54.81830400,56.10914100))); //борисоглебская 3
       locations.add(new Place(new LatLng(54.73491800,55.93197500))); //гафури 101
       locations.add(new Place(new LatLng(54.76921200,56.03454500))); //бульвар тюлькина 3
       locations.add(new Place(new LatLng(54.72933500,55.94131800))); //чернышевского 82
       ArrayList<ArrayList<Double>> dist = GAService.parseLengths(s, locations.size());
       GA ga = new GA(dist, locations.stream().map(x->x.getLocation()).collect(Collectors.toList()));
       List<Integer> order = ga.run();
       for (int i = 0; i < order.size(); ++i)
        {
            log.debug(locations.get(order.get(i)).toString());
        }
       
    }
    
    //@Test
    public void test2()
    {
        ArrayList<Integer> source = new ArrayList<>(Arrays.asList(0,1,2,3,4,5,6,7,8,9,10));
        Collections.reverse(source.subList(4, 7));
        Assert.assertArrayEquals(new Integer[]{0,1,2,3,6,5,4,7,8,9,10}, source.toArray());
    }
    
}
