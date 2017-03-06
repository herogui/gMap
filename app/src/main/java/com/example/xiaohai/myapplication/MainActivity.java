package com.example.xiaohai.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.xiaohai.myapplication.MyEvent.CusEvent;
import com.example.xiaohai.myapplication.MyEvent.CusEventListener;
import com.example.xiaohai.myapplication.MyEvent.EventSourceObject;
import com.example.xiaohai.myapplication.MyEvent.GpsStatusManager;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.PathOverlay;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

//private DefaultResourceProxyImpl resourceProxy;
public class MainActivity extends Activity {

    MapView map;
    private IMapController mapController;
    GPSManager gps;
    GpsStatusManager gpsStatus;
    NetworkLbsManager NetLbs;
    private ItemizedOverlay<OverlayItem> mMyLocationOverlay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context ctx = getApplicationContext();
        //important! set your user agent to prevent getting banned from the osm servers
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_main);

        map = (MapView) findViewById(R.id.map);

       // JZLocationConverter.LatLng  latLng =  JZLocationConverter.gcj02Encrypt(31.15657, 121.48853);

        //GeoPoint  center = new GeoPoint(latLng.getLatitude(),latLng.getLongitude());
        //initMap(center);
        //定位
        gpsStatus = GpsStatusManager.instance(MainActivity.this);

        if(openGPSSettings()) {
            gps = GPSManager.instance(MainActivity.this);
            NetLbs = NetworkLbsManager.instance(MainActivity.this);
            NetLbs.GetResEventInit.addCusListener(cusEventInit);
            NetLbs.startGpsLocate();
        }
    }

    void initMap(GeoPoint center)
    {
        //resourceProxy = new DefaultResourceProxyImpl(this);
        map.setUseDataConnection(false);
        map.setTileSource(TileSourceFactory.getTileSource("Mapnik"));

        mapController = map.getController();

        //center = new GeoPoint(31.15689,121.48948);

        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        map.setClickable(true);
        map.setUseDataConnection(false);

        mapController.setZoom(16);
        mapController.setCenter(center);

        //marker
        final ArrayList<OverlayItem> items = new ArrayList<>();
        items.add(new OverlayItem("Hannover", "SampleDescription",center));

        AddOverLay(items);
    }

    CusEventListener cusEventInit =   new CusEventListener() {
        @Override
        public void fireCusEvent(CusEvent e) {
            EventSourceObject eObject = (EventSourceObject) e.getSource();
            String res = eObject.getString();

            Toast.makeText(MainActivity.this, res, Toast.LENGTH_LONG).show();

            double lon = Double.parseDouble(res.split(",")[0]);
            double lat = Double.parseDouble(res.split(",")[1]);

            JZLocationConverter.LatLng  latLng =  JZLocationConverter.gcj02Encrypt(lat, lon);
            GeoPoint  center = new GeoPoint(latLng.getLatitude(),latLng.getLongitude());
            initMap(center);

            //marker
            final ArrayList<OverlayItem> items = new ArrayList<>();
            items.add(new OverlayItem("Hannover", "SampleDescription",center));

            AddOverLay(items);

            //获取初始化的位置后要停止监听
            NetLbs.GetResEvent.removeListener(cusEventInit);

            NetLbs.GetResEvent.addCusListener(cusEvent);
            gps.GetResEvent.addCusListener(cusEvent);
            gpsStatus.start();
        }
    };

    CusEventListener cusEvent =   new CusEventListener() {
        @Override
        public void fireCusEvent(CusEvent e) {
            EventSourceObject eObject = (EventSourceObject) e.getSource();
            String res = eObject.getString();
            Toast.makeText(MainActivity.this,res, Toast.LENGTH_SHORT).show();
            double lon = Double.parseDouble(res.split(",")[0]);
            double lat = Double.parseDouble(res.split(",")[1]);

            JZLocationConverter.LatLng  latLng =  JZLocationConverter.gcj02Encrypt(lat, lon);
            GeoPoint  center = new GeoPoint(latLng.getLatitude(),latLng.getLongitude());
            //marker
            final ArrayList<OverlayItem> items = new ArrayList<>();
            items.add(new OverlayItem("Hannover", "SampleDescription",center));

            AddOverLay(items);
        }
    };

    void AddOverLay(ArrayList<OverlayItem> items)
    {
			/* OnTapListener for the Markers, shows a simple Toast. */
        this.mMyLocationOverlay = new ItemizedIconOverlay<>(items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        Toast.makeText(
                                MainActivity.this,
                                "Item '" + item.getTitle() + "' (index=" + index
                                        + ") got single tapped up", Toast.LENGTH_LONG).show();
                        return true; // We 'handled' this event.
                    }

                    @Override
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        Toast.makeText(
                                MainActivity.this,
                                "Item '" + item.getTitle() + "' (index=" + index
                                        + ") got long pressed", Toast.LENGTH_LONG).show();
                        return false;
                    }
                }, MainActivity.this.getApplicationContext());
        this.map.getOverlays().add(this.mMyLocationOverlay);
    }

    private boolean openGPSSettings() {
        LocationManager alm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            //Toast.makeText(this, "GPS模块正常" ,Toast.LENGTH_SHORT) .show();
            return true;
        }
        Toast.makeText(this, "请开启GPS！", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        this.startActivityForResult(intent, 0); //此为设置完成后返回到获取界面
        return  false;
    }
    public void onResume() {
        super.onResume();

        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
    }
}

