package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // T Map View
        tMapView = new TMapView(this);
        initTMapView();

        // T Map GPS
        initTMapGPS();

        // Load Database (Market)
        List<Market> marketList = initLoadMarketDatabase();

        // Add Market Marker
        addMarketMarker(marketList);

        // T Map View Using Linear Layout
        LinearLayout linearLayoutTMap = (LinearLayout)findViewById(R.id.linearLayoutTmap);
        linearLayoutTMap.addView(tMapView);
    }

    public void initTMapView(){
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
    }

    public void initTMapGPS(){
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
    }

    @Override
    public void onLocationChange(Location location) {
        tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
        tMapView.setCenterPoint(location.getLongitude(), location.getLatitude());
    }

    public List<Market> initLoadMarketDatabase(){

        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        databaseHelper.OpenDatabaseFile();

        List<Market> marketList = databaseHelper.getTableData();
        Log.e("test", String.valueOf(marketList.size()));

        databaseHelper.close();
        return marketList;
    }

    public void addMarketMarker(List<Market> marketList){

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.markerline_yellow);

        for (int i = 0; i < marketList.size(); i++){

            String storeName = marketList.get(i).storeName;     // 이름
            String address = marketList.get(i).address;         // 주소
            double lat = marketList.get(i).latitude;            // 위도
            double lon = marketList.get(i).longitude;           // 경도

            // TMapPoint
            TMapPoint tMapPoint = new TMapPoint(lat, lon);

            // TMapMarkerItem
            // Marker Initial Settings
            TMapMarkerItem tMapMarkerItem = new TMapMarkerItem();
            tMapMarkerItem.setIcon(bitmap);
            tMapMarkerItem.setPosition(0.5f, 1.0f);
            tMapMarkerItem.setTMapPoint(tMapPoint);
            tMapMarkerItem.setName(storeName);

            // Balloon View Initial Settings
            tMapMarkerItem.setCanShowCallout(true);     // Balloon View 사용
            tMapMarkerItem.setCalloutTitle(storeName);  // Main Message
            tMapMarkerItem.setCalloutSubTitle(address); // Sub Message
            tMapMarkerItem.setAutoCalloutVisible(false); // 초기 접속 시 Balloon View X

            // add Marker on T Map View
            // id로 마커를 식별
            tMapView.addMarkerItem("marketLocation" + i, tMapMarkerItem);
        }

    }

}
