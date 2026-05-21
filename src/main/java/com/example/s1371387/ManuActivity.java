package com.example.s1371387;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;
import java.util.HashSet;

public class ManuActivity extends AppCompatActivity {
    private String TAG = "MainActivity";
    private ListView listView;
    private ImageButton locaBtn, SearBtn, FavBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        listView = (ListView) findViewById(R.id.listview);
        locaBtn = findViewById(R.id.locaBtn);
        SearBtn = findViewById(R.id.SearBtn);
        FavBtn = findViewById(R.id.FavBtn);
        RouteJsonHandlerThread jsonHandlerThread = new RouteJsonHandlerThread();
        jsonHandlerThread.start();
        try {
            jsonHandlerThread.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                BusInfo.BusinfoList,
                R.layout.list_view_layout,
                new String[] { BusInfo.ROUTE, BusInfo.ORIG, BusInfo.DEST },
                new int[] { R.id.route, R.id.orig, R.id.dest }
        );
        listView.setAdapter(adapter);
        SearBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ManuActivity.this, SearchActivity.class);
                startActivity(intent);
                finish();
            }
        });
        locaBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ManuActivity.this, MapActivity.class);
                startActivity(intent);
                finish();
            }
        });
        FavBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ManuActivity.this, FavouriteActivity.class);
                startActivity(intent);
                finish();
            }
        });
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        HashMap<String, String> FavBusInfo = BusInfo.BusinfoList.get(position);
                        String FavBusName = FavBusInfo.get(BusInfo.ROUTE);
                        final String choose[] = {"Add","Remove"};
                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ManuActivity.this);
                        mBuilder.setTitle("Add to favourite");
                        mBuilder.setSingleChoiceItems(choose, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i==0){
                                    if (BusInfo.isFavorite(FavBusName)){
                                        Toast.makeText(ManuActivity.this, FavBusName + " already added to favourites", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        BusInfo.addToFavorites(FavBusName);
                                        HashSet<String> processedRoutes = new HashSet<>();

                                        for (HashMap<String, String> busInfo : BusInfo.FavouriteBusList) {
                                            // Extract the route name
                                            String route = busInfo.get(BusInfo.ROUTE);
                                            String direction = busInfo.get(BusInfo.Direction);
                                            String serviceType = busInfo.get(BusInfo.Service);

                                            // Check if the route is not null and hasn't been processed yet
                                            if (route != null && !processedRoutes.contains(route)) {
                                                // Add the route to the HashSet to mark it as processed
                                                processedRoutes.add(route);

                                                // Create and start the thread
                                                if (direction != null && serviceType != null) {
                                                    StopJsonHandlerThread jsonHandlerThread = new StopJsonHandlerThread(route, direction, serviceType);
                                                    jsonHandlerThread.start();
                                                } else {
                                                    Log.e("BusInfo", "Missing required information for route: " + route);
                                                }
                                            }
                                        }
                                    }
                                }
                                else if(i==1){
                                    BusInfo.removeFromFavorites((FavBusName));

                                }
                                dialogInterface.dismiss();
                            }
                        });
                        mBuilder.create();
                        mBuilder.show();
                    }
                }
        );
    }
}