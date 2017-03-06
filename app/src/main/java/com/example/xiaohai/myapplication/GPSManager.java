package com.example.xiaohai.myapplication;

        import java.util.Set;

        import android.content.Context;
        import android.location.Location;
        import android.location.LocationListener;
        import android.location.LocationManager;
        import android.location.LocationProvider;
        import android.os.Bundle;
        import android.util.Log;

        import com.example.xiaohai.myapplication.MyEvent.EventSourceObject;


public class GPSManager implements LocationListener {
    /**
     * define a Context object
     */
    private static Context context;
    /**
     * define a GPSManager object to manage  GPS location
     */
    private static GPSManager gpsManager = null;
    /**
     * define a LoationManager object to manage GPS function
     */
    private static LocationManager mLocationManager;

    public EventSourceObject GetResEvent = new EventSourceObject();

    /**

     */
    private  GPSManager(Context context) {
        GPSManager.context = context;
    }
    /**
     * method name：instance<BR>
     * method description：the singleton instance of this class<BR>
     * remarks：<BR>
     */
    public static GPSManager instance(Context context) {
        GPSManager.context = context;
        if (gpsManager == null) {
            gpsManager = new GPSManager(context);
        }
        return gpsManager;
    }
    /**
     * method name：startGpsLoate<BR>
     * method description：the method used to request location updates<BR>
     * remarks：<BR>
     */
    public void startGpsLocate() {
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3 * 1000, 2, this);
    }
    /**
     * method name: onLocationChanged<BR>
     * method description：the method used to listen to location change<BR>
     * remarks：<BR>
     */
    @Override
    public void onLocationChanged(Location location) {
        double x = location.getLongitude();
        double y = location.getLatitude();
        GetResEvent.setString(x + "," + y);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            //GPS状态为可见时
            case LocationProvider.AVAILABLE:
                Log.d("gpsstate", "当前GPS状态为可见状态");
                break;
            //GPS状态为服务区外时
            case LocationProvider.OUT_OF_SERVICE:
                Log.d("gpsstate", "当前GPS状态为服务区外状态");
                break;
            //GPS状态为暂停服务时
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.d("gpsstate", "当前GPS状态为暂停服务状态");
                break;
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
        Log.d("gpsstate", "gps is enabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
        Log.d("gpsstate", "gps is onProviderDisabled");
    }
    /**
     * method name: isEnabled<BR>
     * method description：the method used to judge whether the GPS is enabled<BR>
     * remarks：<BR>
     */
    public boolean isEnabled() {
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * method name: endGpsLocate<BR>
     * method description：the method used to listen to location change<BR>
     * remarks：<BR>
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