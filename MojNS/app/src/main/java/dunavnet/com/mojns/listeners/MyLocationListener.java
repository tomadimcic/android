package dunavnet.com.mojns.listeners;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by Tomek on 6.9.2015.
 */
public class MyLocationListener implements LocationListener {

    Location mLocation;

    @Override
    public void onLocationChanged(Location loc) {
        loc.getLatitude();
        loc.getLongitude();
        String text = "My current location is: " + "Latitude = " + loc.getLatitude() + "Longitude = " + loc.getLongitude();

        System.out.println(text);

        setLocation(loc);

    }

    public void setLocation(Location loc){
        mLocation = loc;
    }

    public Location getLocation(){
        return mLocation;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}