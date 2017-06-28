package com.tsp.model;

import com.tsp.geocode.Geocoder;
import java.util.Map;
import javax.persistence.*;

@Entity
@Table(name="Place")
public class Place
{
    @EmbeddedId
    private LatLng location;
    private String address;
    
    public Place()
    {
        location = new LatLng();
        address = "";
    }
    
    public Place(LatLng _location)
    {
        location = _location;
        address = Geocoder.geocode(location);
    }
    
    public Place(String _address)
    {
        Map.Entry<String, LatLng> geocode = Geocoder.geocode(_address);
        address = geocode.getKey();
        location = geocode.getValue();
    }
    
    public Place(String _address, LatLng _location)
    {
        location = _location;
        address = _address;
    }
    
    /**
     * @return the location
     */
    public LatLng getLocation()
    {
        return location;
    }

    /**
     * @return the address
     */
    public String getAddress()
    {
        return address;
    }
    
    @Override
    public String toString()
    {
        return getAddress();
    }
    
    @Override
    public int hashCode()
    {
        return location.hashCode();
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (o == this)
            return true;
        if (!(o instanceof Place))
            return false;
        Place p = (Place)o;
        return this.getLocation().equals(p);
    }
    
    public String toCSV()
    {
        return this.getLocation().getLat() + ";" + this.getLocation().getLng() + ";" + address;
    }
}
