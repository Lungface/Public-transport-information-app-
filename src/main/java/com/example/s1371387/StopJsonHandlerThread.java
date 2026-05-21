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

public class StopJsonHandlerThread extends Thread{
    private static final String TAG = "StopJsonHandlerThread";
    private String route;
    private String direction;
    private String serviceType;
    // URL to get contacts JSON file
    private static String baseStopIDJsonUrl = "https://data.etabus.gov.hk/v1/transport/kmb/route-stop";
    private static String baseStopInfoUrl = "https://data.etabus.gov.hk/v1/transport/kmb/stop";

    public StopJsonHandlerThread(String route, String direction, String serviceType) {
        this.route = route;
        this.direction = direction;
        this.serviceType = serviceType;
    }

    public static String makeRequest(String jsonUrl) {
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
        String jsonUrl = baseStopIDJsonUrl + "/" + route + "/" + direction + "/" + serviceType;
        String StopInfoIDStr = makeRequest(jsonUrl);
        Log.e(TAG, "Response from url: " + StopInfoIDStr);
        if (StopInfoIDStr != null){
            try {
                JSONObject jsonObj = new JSONObject(StopInfoIDStr);

                // Getting JSON Array node
                JSONArray BusStopInfos = jsonObj.getJSONArray("data");
                for (int i = 0; i < BusStopInfos.length(); i++) {
                    JSONObject c = BusStopInfos.getJSONObject(i);
                    String route = c.getString("route");
                    String stopid = c.getString("stop");
                    StopInfo.addRouteStopID(route, stopid);
                }
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "Couldn't get json from server.");
        }
        if (StopInfo.RouteStopNameInfo.isEmpty()){
            String StopInfoNameStr = makeRequest(baseStopInfoUrl);
            Log.e(TAG, "Response from url: " + StopInfoNameStr);
            if (StopInfoNameStr != null){
                try {
                    JSONObject jsonObj = new JSONObject(StopInfoNameStr);

                    // Getting JSON Array node
                    JSONArray BusStopNameInfos = jsonObj.getJSONArray("data");
                    for (int i = 0; i < BusStopNameInfos.length(); i++) {
                        JSONObject c = BusStopNameInfos.getJSONObject(i);
                        String stopid = c.getString("stop");
                        String name = RouteJsonHandlerThread.isEnglish ? c.getString("name_en") : c.getString("name_tc");
                        String lat = c.getString("lat");
                        String log = c.getString("long");
                        StopInfo.addRouteStopName(stopid, name, lat, log);

                    }
                    StopInfo.matchRouteStopInfo();
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
            }
        }
    }
}