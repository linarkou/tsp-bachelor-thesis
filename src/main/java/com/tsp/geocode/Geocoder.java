package com.tsp.geocode;

import com.tsp.model.LatLng;
import com.google.gson.*;
import com.tsp.model.Place;
import java.io.*;
import java.net.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author polzovatel
 */
public class Geocoder
{
    /*public static LatLng geocode(String address)
    {
        LatLng location = new LatLng(0, 0);
        try
        {
            String url = "https://geocode-maps.yandex.ru/1.x/?format=json&geocode=" + address.replaceAll("[^-//№а-яА-Яa-zA-Z0-9]+", "+")+"&results=1";
            InputStreamReader in = new InputStreamReader((new URL(url)).openStream());
            JsonElement jelement = new JsonParser().parse(in);
            JsonObject  jobject = jelement.getAsJsonObject();
            jobject = jobject.getAsJsonObject("response").getAsJsonObject("GeoObjectCollection");
            JsonArray jarray = jobject.getAsJsonArray("featureMember");
            jobject = jarray.get(0).getAsJsonObject().getAsJsonObject("GeoObject").getAsJsonObject("Point");
            String result = jobject.get("pos").toString();
            String[] split = result.split(" ");
            if (split.length>=2) {
                location.setLng(Double.valueOf(split[0].replaceAll("[^\\d\\.]","")));
                location.setLat(Double.valueOf(split[1].replaceAll("[^\\d\\.]","")));
            }
        } catch (Exception ex)
        {
            Logger.getLogger(Geocoder.class.getName()).log(Level.SEVERE, "Bad URL or No place with this address", ex);
        }
        return location;
    }*/
    
    public static String geocode(LatLng location)
    {
        String name="";
        try
        {
            String url = "https://geocode-maps.yandex.ru/1.x/?format=json&geocode=" + location.toString()+"&results=1";
            InputStreamReader in = new InputStreamReader((new URL(url)).openStream());
            JsonElement jelement = new JsonParser().parse(in);
            JsonObject  jobject = jelement.getAsJsonObject();
            jobject = jobject.getAsJsonObject("response").getAsJsonObject("GeoObjectCollection");
            JsonArray jarray = jobject.getAsJsonArray("featureMember");
            jobject = jarray.get(0).getAsJsonObject().getAsJsonObject("GeoObject").getAsJsonObject("metaDataProperty").getAsJsonObject("GeocoderMetaData");
            name = jobject.get("text").toString();
        } catch (Exception ex)
        {
            Logger.getLogger(Geocoder.class.getName()).log(Level.SEVERE, "Unknown exception :D", ex);
        }
        return name;
    }
    
    public static Entry<String, LatLng> geocode(String address)
    {
        LatLng location = new LatLng(0, 0);
        String name = "";
        try
        {
            String url = "https://geocode-maps.yandex.ru/1.x/?format=json&geocode=" + address.replaceAll("[^-//№а-яА-Яa-zA-Z0-9]+", "+")+"&results=1";
            InputStreamReader in = new InputStreamReader((new URL(url)).openStream());
            JsonElement jelement = new JsonParser().parse(in);
            JsonObject  jobject = jelement.getAsJsonObject();
            jobject = jobject.getAsJsonObject("response").getAsJsonObject("GeoObjectCollection");
            JsonArray jarray = jobject.getAsJsonArray("featureMember");
            jobject = jarray.get(0).getAsJsonObject().getAsJsonObject("GeoObject");
            String point = jobject.getAsJsonObject("Point").get("pos").toString();
            String[] split = point.split(" ");
            if (split.length>=2) {
                location.setLng(Double.valueOf(split[0].replaceAll("[^\\d\\.]","")));
                location.setLat(Double.valueOf(split[1].replaceAll("[^\\d\\.]","")));
            }
            name = jobject.getAsJsonObject("metaDataProperty").getAsJsonObject("GeocoderMetaData").get("text").toString();
        } catch (Exception ex)
        {
            Logger.getLogger(Geocoder.class.getName()).log(Level.SEVERE, "Bad URL or No place with this address: "+address, ex);
        }
        return new SimpleEntry<String, LatLng>(name,location);
    }
}
