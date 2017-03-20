package com.example.aijaz.map1;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by aijaz on 3/19/17.
 */

public class Utility {

    public static void getNearByTransits(GoogleMap mMap, String url, Location myLocation, List<NearByTransit> nearByTransitList){
        String googlePlacesData;
        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googlePlacesData = downloadUrl.readUrl(url);
            List<HashMap<String, String>> nearbyPlacesList = null;
            DataParser dataParser = new DataParser();
            nearbyPlacesList = dataParser.parse(googlePlacesData);


            for (int i = 0; i < nearbyPlacesList.size(); i++) {
                HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
                double lat = Double.parseDouble(googlePlace.get("lat"));
                double lng = Double.parseDouble(googlePlace.get("lng"));

                LatLng origin = new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
                LatLng pos = new LatLng(lat,lng);

                NearByTransit nearByTransit = new NearByTransit(origin,pos,url);
                nearByTransitList.add(nearByTransit);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    public static String getUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        String mode = "mode=bicycling";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    public static void getDurationForDestination(LatLng dest, ArrayList<NearByTransit> nearByTransitArrayList) {
        for(NearByTransit nearByTransit : nearByTransitArrayList){
            nearByTransit.setDestnation(dest);

            String url = Utility.getUrl(nearByTransit.getPos(),dest);
            Object[] DataTransfer = new Object[4];
            DataTransfer[0] = null;
            DataTransfer[1] = url;
            DataTransfer[2] = nearByTransit;
            DataTransfer[3] = true;
            FetchUrl FetchUrl = new FetchUrl();

            // Start downloading json data from Google Directions API
            FetchUrl.execute(DataTransfer);
        }

        Log.d("Get_Duration_Dest","Got Duration for total travel");
    }
}
