package com.example.s1371387;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class FavouriteActivity extends AppCompatActivity {
    private Button FavBaBtn;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favourite);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        listView = findViewById(R.id.listview);

        SimpleAdapter adapter = new SimpleAdapter(
                FavouriteActivity.this,
                BusInfo.FavouriteBusList,
                R.layout.list_view_layout,
                new String[]{BusInfo.ROUTE, BusInfo.ORIG, BusInfo.DEST},
                new int[]{R.id.route, R.id.orig, R.id.dest}
        );

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Get the clicked item's information
                        HashMap<String, String> busInfo = StopInfo.MatchedRouteStopInfo.get(position);

                        // Create an AlertDialog to display the information
                        AlertDialog.Builder builder = new AlertDialog.Builder(FavouriteActivity.this);
                        builder.setTitle(busInfo.get(StopInfo.ROUTE));

                        // Loop through the MatchedRouteStopInfo list to display all stops
                        StringBuilder messageBuilder = new StringBuilder();
                        for (HashMap<String, String> stopInfo : StopInfo.MatchedRouteStopInfo) {
                            messageBuilder.append("Stop: ").append(stopInfo.get(StopInfo.NAME)).append("\n");
                        }

                        // Set the message in the AlertDialog
                        builder.setMessage(messageBuilder.toString());

                        // Show the AlertDialog
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                }
        );

        FavBaBtn = findViewById(R.id.FavBackBtn);
        FavBaBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(FavouriteActivity.this, ManuActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}