package com.example.xiaohai.myapplication.MyEvent;

import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;



import java.util.Iterator;

/**
 * Created by giser on 2015/6/9.
 */
public class GpsStatusManager{
    private static Context context;
    private static GpsStatusManager gpsStatusManager = null;
    private static LocationManager mLocationManager;
    public EventSourceObject GetResEvent = new EventSourceObject();
    private  GpsStatusManager(Context context) {
        GpsStatusManager.context = context;
    }
    /**
     * method name：instance<BR>
     * method description：the singleton instance of this class<BR>
     * remarks：<BR>
     */
    public static GpsStatusManager instance(Context context) {
        GpsStatusManager.context = context;
        if (gpsStatusManager == null) {
            gpsStatusManager = new GpsStatusManager(context);
        }
        return gpsStatusManager;
    }

    public void start() {
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.addGpsStatusListener(statusListener);//侦听GPS状态
    }

    public void stop()
    {
        mLocationManager.removeGpsStatusListener(statusListener);
    }

    private GpsStatus.Listener statusListener = new GpsStatus.Listener()
    {
        public void onGpsStatusChanged(int event)
        {
            // TODO Auto-generated method stub
            GpsStatus gpsStatus= mLocationManager.getGpsStatus(null);
            switch(event)
            {
                case GpsStatus.GPS_EVENT_FIRST_FIX:{
                    //第一次定位时间UTC gps可用
                    //Log.v(TAG,"GPS is usable");
                    int i=gpsStatus.getTimeToFirstFix();
                    //Toast.makeText(GpsStatusManager.context, "GPS first run  ！" + i, Toast.LENGTH_LONG).show();
                    GetResEvent.setString("GPS first run  ！");
                    break;
                }

                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:{//周期的报告卫星状态
                    //得到所有收到的卫星的信息，包括 卫星的高度角、方位角、信噪比、和伪随机号（及卫星编号）
                    Iterable<GpsSatellite> allSatellites = gpsStatus.getSatellites();
                    Iterator<GpsSatellite> iterator = allSatellites.iterator();
                    int numOfSatellites = 0;
                    int maxSatellites=gpsStatus.getMaxSatellites();

                    while(iterator.hasNext() && numOfSatellites<maxSatellites){
                        numOfSatellites++;
                        GpsSatellite gps = iterator.next();
                        float ft=  gps.getElevation();
                    }
                    //Toast.makeText(GpsStatusManager.context, "卫星数:"+ numOfSatellites, Toast.LENGTH_LONG).show();
                    Log.d("gpsstate", "Satellites num" + numOfSatellites);
                    GetResEvent.setString("Satellites num is " + numOfSatellites);
                    break;
                }
                case GpsStatus.GPS_EVENT_STARTED:{
                    //Toast.makeText(GpsStatusManager.context, "GPS开启！", Toast.LENGTH_LONG).show();
                    Log.d("gpsstate", "GPS run");
                    GetResEvent.setString("gps run");
                    break;
                }
                case GpsStatus.GPS_EVENT_STOPPED:{
                    //Toast.makeText(GpsStatusManager.context, "GPS停止！", Toast.LENGTH_LONG).show();
                    Log.d("gpsstate", "GPS stop");
                    GetResEvent.setString("gps stop");
                    break;
                }
                default :
                    break;
            }
        }
    };
}
