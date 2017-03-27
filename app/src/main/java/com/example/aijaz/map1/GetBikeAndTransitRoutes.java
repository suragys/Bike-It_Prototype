package com.example.aijaz.map1;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by navneet on 23/7/16.
 */
public class GetBikeAndTransitRoutes extends AsyncTask<Object, String, String> {

    String googlePlacesData;
    GoogleMap mMap;

    ArrayList<NearByTransit> nearByTransitArrayList;
    LatLng source;
    LatLng dest;

    @Override
    protected String doInBackground(Object... params) {
        try {
            Log.d("GetBikeAndTransitRoutes", "doInBackground entered");

            mMap = (GoogleMap) params[0];
            source = (LatLng) params[1];
            dest = (LatLng) params[2];
            nearByTransitArrayList = (ArrayList<NearByTransit>) params[3];

            String url = Utility.getUrl(source.latitude, source.longitude, "transit_station");


            DownloadUrl downloadUrl = new DownloadUrl();
            googlePlacesData = downloadUrl.readUrl(url);
            Log.d("GetBikeAndTransitRoutes", "doInBackground Exit");
        } catch (Exception e) {
            Log.d("GooglePlacesReadTask", e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("GooglePlacesReadTask", "onPostExecute Entered");
        List<HashMap<String, String>> nearbyPlacesList = null;
        DataParser dataParser = new DataParser();
        nearbyPlacesList = dataParser.parse(result);
        ShowNearbyPlaces(nearbyPlacesList);
        computeTop2(nearByTransitArrayList);

        Log.d("GooglePlacesReadTask", "onPostExecute Exit");
    }

    private void computeTop2(ArrayList<NearByTransit> nearByTransitArrayList) {
        for (NearByTransit n :
                nearByTransitArrayList) {
            int totalTime = 0;
            if(n.getCycDuration() != null && n.getTransitTime() != null){
                String cycTime = n.getCycDuration();
                String transTime = n.getTransitTime();
                int c = Utility.getTimeInMin(cycTime);
                int t = Utility.getTimeInMin(transTime);
                totalTime = c + t;
                n.setCycTimeInMin(c);
                n.setTotalTimeInMin(totalTime);
            }
        }

        NearByTransit min1 = nearByTransitArrayList.get(0);
        for(NearByTransit n : nearByTransitArrayList){
            if(n.getTotalTimeInMin()!= 0 && n.getTotalTimeInMin() < min1.getTotalTimeInMin()){
                min1 = n;
            }
        }

        mMap.addPolyline(min1.getPolyLineOptions());
        mMap.addPolyline(min1.getPolylineOptionsToDest());
    }

    private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {
        Log.e("SIZE of nearby list====", "" + nearbyPlacesList.size());
        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            Log.d("onPostExecute", "Entered into showing locations");

            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));

            NearByTransit nearByTransit = new NearByTransit(source, new LatLng(lat, lng));
            getCyclingTime(nearByTransit);
            getDestTime(nearByTransit);
            nearByTransitArrayList.add(nearByTransit);



//            Object[] DataTransfer = new Object[3];
//            DataTransfer[0] = mMap;
//            DataTransfer[1] = url;
//
//
//            NearByTransit nearByTransit = new NearByTransit(origin,dest,url);
//            nearByTransitArrayList.add(nearByTransit);
//            Log.d("On_get_nearby_places", "added nearByTransit" + nearByTransitArrayList.size());
//            DataTransfer[2] = nearByTransit;
//            FetchUrl FetchUrl = new FetchUrl();
//
//            // Start downloading json data from Google Directions API
//            FetchUrl.execute(DataTransfer);
//            //move map camera
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        }
    }



    private void getDestTime(NearByTransit nearByTransit) {
        String jsonData = "";
        DownloadUrl downloadUrl = new DownloadUrl();
        try {
//            jsonData = downloadUrl.readUrl(nearByTransit.getUrl());
            FetchUrl2 fetchUrl = new FetchUrl2();
            String url = Utility.getUrl(nearByTransit.getPos(), dest,"transit");
            Log.d("GetBikeAnTransitRoute", url);
            fetchUrl.execute(url);
            jsonData = fetchUrl.get();
            if (!jsonData.contains("error_message")) {
                Log.d("Fetch Url Success", "number for" + nearByTransit.getPos().toString());
                JSONObject jsonObject = new JSONObject(jsonData);
                Log.d("ParserTask", jsonData.toString());
                DataParser2 parser = new DataParser2();
                // Starts parsing data
                String json = jsonObject.toString();
                List<List<HashMap<String, String>>> routes = parser.parse(jsonObject);
                String time = parser.getDurations(jsonObject);
                nearByTransit.setTransitTime(time);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());
                ArrayList<LatLng> points;
                PolylineOptions lineOptions = null;

                // Traversing through all the routes
                for (int i = 0; i < routes.size(); i++) {
                    points = new ArrayList<>();
                    lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = routes.get(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(10);
                    lineOptions.color(Color.RED);

                    Log.d("onPostExecute", "onPostExecute lineoptions decoded");

                }

                nearByTransit.setPolylineOptionsToDest(lineOptions);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    private void getCyclingTime(NearByTransit nearByTransit) {
        // get the cycling route
        // and cycling time

        String jsonData = "";
        
        try {

            FetchUrl2 fetchUrl = new FetchUrl2();
            String url = Utility.getUrl(source, nearByTransit.getPos(),"bicycling");
            Log.d("GetBikeAnTransitRoute", url);
            fetchUrl.execute(url);
            jsonData = fetchUrl.get();
            if (!jsonData.contains("error_message")) {
                Log.d("Fetch Url Success", "number for" + nearByTransit.getPos().toString());
                JSONObject jsonObject = new JSONObject(jsonData);
                Log.d("ParserTask", jsonData.toString());
                DataParser2 parser = new DataParser2();

                // Starts parsing data
                String json = jsonObject.toString();
                List<List<HashMap<String, String>>> routes = parser.parse(jsonObject);
                String time = parser.getDurations(jsonObject);
                nearByTransit.setCycDuration(time);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());
                ArrayList<LatLng> points;
                PolylineOptions lineOptions = null;

                // Traversing through all the routes
                for (int k = 0; k < routes.size(); k++) {
                    points = new ArrayList<>();
                    lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = routes.get(k);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double la = Double.parseDouble(point.get("lat"));
                        double ln = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(la, ln);

                        points.add(position);
                    }

                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(10);
                    lineOptions.color(Color.BLUE);

                    Log.d("onPostExecute", "onPostExecute line options decoded");

                }

                nearByTransit.setPolyLineOptions(lineOptions);

            } else {
                Log.d("Fetch Url Error", "number for" + nearByTransit.getPos().toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
