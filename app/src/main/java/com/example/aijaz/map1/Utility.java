package com.example.aijaz.map1;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by aijaz on 3/19/17.
 */

public class Utility {

    public static String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
//        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append(("&rankby=" + "distance"));
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyDIdqc_BwvnRC24_FwS4-oSITnKFT1N5AY");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    public static String getUrl(LatLng origin, LatLng dest, String mode) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        mode = "mode=" + mode;

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    public static void getBikeAndTransitRoutes(LatLng dest, ArrayList<NearByTransit> nearByTransitArrayList, GoogleMap map, Location origin) {


        LatLng source = new LatLng(origin.getLatitude(), origin.getLongitude());
        Object[] DataTransfer = new Object[4];
        DataTransfer[0] = map;
        DataTransfer[1] = source;
        DataTransfer[2] = dest;
        DataTransfer[3] = nearByTransitArrayList;

        GetBikeAndTransitRoutes getBikeAndTransitRoutes = new GetBikeAndTransitRoutes();
        getBikeAndTransitRoutes.execute(DataTransfer);
        Log.d("Get_Duration_Dest", "Got Duration for total travel");


    }

    public static int getTimeInMin(String s) {
        String[] a = s.split(" ");

        return Integer.parseInt(a[0]);
    }
}
