package com.example.s1371387;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchActivity extends AppCompatActivity {
    private ListView listView;
    private EditText inputText;
    private Button InBtn, BaBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialize views
        listView = findViewById(R.id.listview);
        inputText = findViewById(R.id.input);
        InBtn = findViewById(R.id.EnterButton); // Initialize the button
        BaBtn = findViewById(R.id.Backbutton);
        // Set up button click listener
        InBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String input = inputText.getText().toString().trim().toUpperCase();

                // Validate input
                if (input.isEmpty()) {
                    Toast.makeText(SearchActivity.this, "Please enter a route", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Get filtered bus info
                ArrayList<HashMap<String, String>> filteredBusInfo = BusInfo.getBusInfoByRoute(input);

                if (!filteredBusInfo.isEmpty()) {
                    // Create the SimpleAdapter using the filtered list
                    SimpleAdapter adapter = new SimpleAdapter(
                            SearchActivity.this, // Use the activity context
                            filteredBusInfo, // Use the filtered list
                            R.layout.list_view_layout,
                            new String[]{BusInfo.ROUTE, BusInfo.ORIG, BusInfo.DEST},
                            new int[]{R.id.route, R.id.orig, R.id.dest}
                    );

                    // Set the adapter to the ListView
                    listView.setAdapter(adapter);
                } else {
                    // Handle the case where no matching routes are found
                    Toast.makeText(SearchActivity.this, "No routes found for " + input, Toast.LENGTH_SHORT).show();
                }
            }
        });
        BaBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, ManuActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}