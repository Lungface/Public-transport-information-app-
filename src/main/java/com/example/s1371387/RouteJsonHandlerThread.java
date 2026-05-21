package com.example.s1371387;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class RouteJsonHandlerThread extends Thread {
    private static final String TAG = "RouteJsonHandlerThread";
    public static boolean isEnglish = true;
    // URL to get contacts JSON file
    private static String jsonUrl = "https://data.etabus.gov.hk/v1/transport/kmb/route/";

    public static String makeRequest() {
        String response = null;
        try {
            URL url = new URL(jsonUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = inputStreamToString(in);
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }

    private static String inputStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = "";

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage());
            }
        }
        return sb.toString();
    }

    public void run() {
        if (BusInfo.BusinfoList.isEmpty()){
            String BusInfoStr = makeRequest();
            Log.e(TAG, "Response from url: " + BusInfoStr);

            if (BusInfoStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(BusInfoStr);

                    // Getting JSON Array node
                    JSONArray BusInfos = jsonObj.getJSONArray("data");
                    for (int i = 0; i < BusInfos.length(); i++) {
                        JSONObject c = BusInfos.getJSONObject(i);

                        String route = c.getString("route");
                        String orig = isEnglish ? c.getString("orig_en") : c.getString("orig_tc");
                        String dest = isEnglish ? c.getString("dest_en") : c.getString("dest_tc");
                        String serv = c.getString("service_type");
                        String dire = c.getString("bound");
                        // Add contact (name, email, address) to contact list
                        BusInfo.addBusinfo(route, orig, dest, serv, dire);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
            }
        }
    }
}
