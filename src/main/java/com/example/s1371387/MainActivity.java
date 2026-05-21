package com.example.s1371387;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Button EngBtn, ChiBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize buttons
        EngBtn = findViewById(R.id.Englishbutton);
        ChiBtn = findViewById(R.id.Chinesebutton);

        // Set click listener for English button
        EngBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setLocale("en","us");
                RouteJsonHandlerThread.isEnglish = true;
            }
        });

        // Set click listener for Chinese button
        ChiBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setLocale("zh","HK");
                RouteJsonHandlerThread.isEnglish = false;
            }
        });
    }
    private void setLocale(String languageCode, String countryCode) {
        // Create a new Locale object with language and country
        Locale locale = new Locale(languageCode, countryCode);
        Locale.setDefault(locale);

        // Update the configuration with the new locale
        Configuration config = new Configuration();
        config.setLocale(locale);

        // Apply the configuration to the app's resources
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Start the Manuactivity
        Intent intent = new Intent(MainActivity.this, ManuActivity.class);
        startActivity(intent);
        finish();
    }
}