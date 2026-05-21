package com.example.s1371387;

import java.util.ArrayList;
import java.util.HashMap;

public class BusInfo {
    public static String ROUTE = "route";
    public static String ORIG = "orig";
    public static String DEST = "dest";
    public static String Service = "serv";
    public static String Direction = "dire";
    public static ArrayList<HashMap<String, String>> BusinfoList = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> FavouriteBusList = new ArrayList<>();

    // Creates and add contact to contact list
    public static void addBusinfo(String route, String orig, String dest, String serv, String dire) {
        // Create contact
        HashMap<String, String> Info = new HashMap<>();
        Info.put(ROUTE, route);
        Info.put(ORIG, orig);
        Info.put(DEST, dest);
        Info.put(Service, serv);
        if (dire == "I"){
           dire = "inbound";
        }else {
           dire = "outbound";
        }
        Info.put(Direction, dire);

        // Add contact to contact list
        BusinfoList.add(Info);
    }
    public static ArrayList<HashMap<String, String>> getBusInfoByRoute(String inputRoute) {
        ArrayList<HashMap<String, String>> matchingRoutes = new ArrayList<>();
        for (HashMap<String, String> busInfo : BusinfoList) {
            // Check if the route matches the input
            if (busInfo.get(ROUTE).equals(inputRoute)) {
                matchingRoutes.add(busInfo); // Return the matching bus info
            }
        }
        return matchingRoutes; // Return null if no match is found
    }
    public static void addToFavorites(String route) {
        ArrayList<HashMap<String, String>> busInfoList = getBusInfoByRoute(route);
        for (HashMap<String, String> busInfo : busInfoList) {
            // Add each matching route to the favorites list if it's not already there
            if (!FavouriteBusList.contains(busInfo)) {
                FavouriteBusList.add(busInfo);
            }
        }
    }
    public static void removeFromFavorites(String route) {
        ArrayList<HashMap<String, String>> busInfoList = getBusInfoByRoute(route);
        for (HashMap<String, String> busInfo : busInfoList) {
            // Remove each matching route from the favorites list
            FavouriteBusList.remove(busInfo);
        }
    }
    public static boolean isFavorite(String route) {
        ArrayList<HashMap<String, String>> busInfoList = getBusInfoByRoute(route);
        for (HashMap<String, String> busInfo : busInfoList) {
            // Check if any matching route is in the favorites list
            if (FavouriteBusList.contains(busInfo)) {
                return true;
            }
        }
        return false;
    }
}