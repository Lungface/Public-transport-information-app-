package com.example.s1371387;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class StopInfo {
    public static final String ROUTE = "route";
    public static final String STOPID = "stopid";
    public static final String NAME = "name";
    public static final String LAT = "lat";
    public static final String LONG = "log";

    // Static lists to store route and stop information
    public static ArrayList<HashMap<String, String>> RouteStopInfo = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> RouteStopNameInfo = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> MatchedRouteStopInfo = new ArrayList<>();

    // Method to add route stop ID information
    public static void addRouteStopID(String route, String stopid) {
        HashMap<String, String> info = new HashMap<>();
        info.put(ROUTE, route);
        info.put(STOPID, stopid);

        RouteStopInfo.add(info);
    }

    // Method to add route stop name information
    public static void addRouteStopName(String stopid, String name, String lat, String log) {
        HashMap<String, String> info = new HashMap<>();
        info.put(STOPID, stopid);
        info.put(NAME, name);
        info.put(LAT, lat);
        info.put(LONG, log);

        RouteStopNameInfo.add(info);
    }

    // Method to match route stop information with stop name details
    public static void matchRouteStopInfo() {
        //MatchedRouteStopInfo.clear(); // Clear previous matches

        for (HashMap<String, String> routeStop : RouteStopInfo) {
            String stopId = routeStop.get(STOPID);

            for (HashMap<String, String> stopNameInfo : RouteStopNameInfo) {
                if (stopNameInfo.get(STOPID).equals(stopId)) {
                    // Combine the information
                    HashMap<String, String> combinedInfo = new HashMap<>(routeStop);
                    combinedInfo.put(NAME, stopNameInfo.get(NAME));
                    combinedInfo.put(LAT, stopNameInfo.get(LAT));
                    combinedInfo.put(LONG, stopNameInfo.get(LONG));

                    MatchedRouteStopInfo.add(combinedInfo);
                }
            }
        }
    }
}