package com.tsp.model;

import java.io.Serializable;
import java.util.Locale;
import javax.persistence.*;

/**
 * A place on Earth, represented by a Latitude/Longitude pair.
 */
@Embeddable
//@Entity
//@Table(name = "LatLng")
public class LatLng implements Serializable {

  /**
   * The latitude of this location.
   */
  
  private double lat;

  /**
   * The longitude of this location.
   */
  
  private double lng;

  /**
   * Construct a location with a latitude longitude pair.
   */
  public LatLng() {
    this.lat = 0;
    this.lng = 0;
  }
  
  public LatLng(double lat, double lng) {
    this.lat = lat;
    this.lng = lng;
  }

  @Override
  public String toString() {
    return toUrlValue();
  }

  public String toUrlValue() {
    // Enforce Locale to English for double to string conversion
    return String.format(Locale.ENGLISH, "%.8f,%.8f", getLat(), getLng());
  }
  
  public String toLngLat() {
       return String.format(Locale.ENGLISH, "%.8f,%.8f", getLng(), getLat());
  }

    /**
     * @return the lat
     */
    public double getLat()
    {
        return lat;
    }

    /**
     * @return the lng
     */
    public double getLng()
    {
        return lng;
    }

    /**
     * @param lat the lat to set
     */
    public void setLat(double lat)
    {
        this.lat = lat;
    }

    /**
     * @param lng the lng to set
     */
    public void setLng(double lng)
    {
        this.lng = lng;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (o == this)
            return true;
        if (!(o instanceof LatLng))
            return false;
        LatLng ll = (LatLng)o;
        if (ll.getLat() == this.getLat() && ll.getLng() == this.getLng())
            return true;
        else
            return false;
    }
    
    public double haversineDist(LatLng ll2) {
        double dLat = Math.toRadians(ll2.lat - lat);
        double dLon = Math.toRadians(ll2.lng - lng);
        double lat1 = Math.toRadians(this.lat);
        double lat2 = Math.toRadians(ll2.lat);
 
        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return 6372.8 * c;
    }
}
