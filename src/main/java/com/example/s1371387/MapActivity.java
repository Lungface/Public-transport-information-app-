package com.example.s1371387;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap gMap;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        button = findViewById(R.id.Backbutton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, ManuActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        if (BusInfo.FavouriteBusList.isEmpty()){
            LatLng location = new LatLng(22.302711, 114.177216);
            googleMap.addMarker(new MarkerOptions().position(location));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12));
        }
        else{
            for (HashMap<String, String> stopInfo : StopInfo.MatchedRouteStopInfo) {
                // Extract latitude and longitude from the stopInfo HashMap
                double latitude = Double.parseDouble(stopInfo.get(StopInfo.LAT)); // Replace "LAT" with the actual key for latitude
                double longitude = Double.parseDouble(stopInfo.get(StopInfo.LONG)); // Replace "LON" with the actual key for longitude

                // Create a LatLng object for the stop
                LatLng stopLocation = new LatLng(latitude, longitude);

                // Add a marker for the stop
                googleMap.addMarker(new MarkerOptions()
                        .position(stopLocation)
                        .title(stopInfo.get(StopInfo.NAME)));
                if (!StopInfo.MatchedRouteStopInfo.isEmpty()) {
                    HashMap<String, String> firstStop = StopInfo.MatchedRouteStopInfo.get(0);
                    double firstLatitude = Double.parseDouble(stopInfo.get(StopInfo.LAT));
                    double firstLongitude = Double.parseDouble(stopInfo.get(StopInfo.LONG));
                    LatLng firstLocation = new LatLng(firstLatitude, firstLongitude);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 18));
                }
            }
        }
    }
}