package com.example.xiaohai.myapplication;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;


import com.example.xiaohai.myapplication.MyEvent.EventSourceObject;


public class NetworkLbsManager implements LocationListener {
    /**
     * define a Context object
     */
    private static Context context;
    /**
     * define a GPSManager object to manage  GPS location
     */
    private static NetworkLbsManager gpsManager = null;
    /**
     * define a LoationManager object to manage GPS function
     */
    private static LocationManager mLocationManager;

    public EventSourceObject GetResEvent = new EventSourceObject();
    public EventSourceObject GetResEventInit = new EventSourceObject();

    /**
     */
    private NetworkLbsManager(Context context) {
        NetworkLbsManager.context = context;
    }
    /**
     * method name£ºinstance<BR>
     * method description£ºthe singleton instance of this class<BR>
     * remarks£º<BR>
     */
    public static NetworkLbsManager instance(Context context) {
        NetworkLbsManager.context = context;
        if (gpsManager == null) {
            gpsManager = new NetworkLbsManager(context);
        }
        return gpsManager;
    }
    /**
     * method name£ºstartGpsLoate<BR>
     * method description£ºthe method used to request location updates<BR>
     * remarks£º<BR>
     */
    public void startGpsLocate() {
//        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3 * 1000, 2, this);
    }
    /**
     * method name: onLocationChanged<BR>
     * method description£ºthe method used to listen to location change<BR>
     * remarks£º<BR>
     */
    @Override
    public void onLocationChanged(Location location) {
        double x = location.getLongitude();
        double y = location.getLatitude();
        GetResEvent.setString(x + "," +y);
        GetResEventInit.setString(x + "," +y);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
        Log.d("jfttcjl", "gps is enabled");
        this.startGpsLocate();
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }
    /**
     * method name: isEnabled<BR>
     * method description£ºthe method used to judge whether the GPS is enabled<BR>
     * remarks£º<BR>
     */
    public boolean isEnabled() {
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d("jfttcjl", "gps is on");
            return true;
        } else {
            return false;
        }
    }
    /**
     * method name: endGpsLocate<BR>
     * method description£ºthe method used to listen to location change<BR>
     * remarks£º<BR>
     *
     * (none)
     */
    public void closeGpsLocate() {
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(this);
            mLocationManager = null;
        }
    }
}