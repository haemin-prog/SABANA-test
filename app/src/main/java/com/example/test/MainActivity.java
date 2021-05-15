package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback {

    String API_Key = "l7xx97cfc10b259149a78f176bd0fd0048c1";

    // T Map View
    TMapView tMapView = null;

    // T Map GPS
    TMapGpsManager tMapGPS = null;

    // Initial Location
    double initialLatitude = 37.451711825486946;
    double initialLongitude = 126.65661030197585;

    public List<Market> marketList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // T Map View
        tMapView = new TMapView(this);

        // API Key
        tMapView.setSKTMapApiKey(API_Key);

        // Initial Setting
        tMapView.setZoomLevel(17);
        tMapView.setIconVisibility(true);
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);

        // Initial Location Setting
        tMapView.setLocationPoint(initialLongitude, initialLatitude);
        tMapView.setCenterPoint(initialLongitude, initialLatitude);

        // T Map View Using Linear Layout
        LinearLayout linearLayoutTmap = (LinearLayout)findViewById(R.id.linearLayoutTmap);
        linearLayoutTmap.addView(tMapView);

        // Request For GPS permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        // T Map GPS
        tMapGPS = new TMapGpsManager(this);

        // Initial Setting
        tMapGPS.setMinTime(1000);
        tMapGPS.setMinDistance(10);
        tMapGPS.setProvider(tMapGPS.NETWORK_PROVIDER);
        //tMapGPS.setProvider(tMapGPS.GPS_PROVIDER);

        // Using GPS
        tMapGPS.OpenGps();

        // Load Initial Database (Market)
        initLoadMarketDatabase();

    }

    @Override
    public void onLocationChange(Location location) {
        tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
        tMapView.setCenterPoint(location.getLongitude(), location.getLatitude());
    }

    // Load Database
    private void initLoadMarketDatabase(){

        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        databaseHelper.CreateDatabase();
        databaseHelper.OpenDatabase();

        marketList = databaseHelper.getTableData();

        databaseHelper.close();

    }

}
