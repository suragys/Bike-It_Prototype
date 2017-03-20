package com.example.aijaz.map1;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

/**
 * Created by aijaz on 3/19/17.
 */

public class NearByTransit {

    private LatLng origin;
    private LatLng pos;
    private LatLng destnation;
    private int timeInMin;
    private String duration;
    private String url;

    private JSONObject apiResponse;
    private PolylineOptions polyLineOptions;

    public NearByTransit(LatLng origin, LatLng pos, String url) {
        this.origin = origin;
        this.pos = pos;
        this.url = url;

    }

    public LatLng getOrigin() {
        return origin;
    }

    public void setOrigin(LatLng origin) {
        this.origin = origin;
    }

    public LatLng getPos() {
        return pos;
    }

    public void setPos(LatLng pos) {
        this.pos = pos;
    }

    public LatLng getDestnation() {
        return destnation;
    }

    public void setDestnation(LatLng destnation) {
        this.destnation = destnation;
    }

    public int getTimeInMin() {
        return timeInMin;
    }

    public void setTimeInMin(int timeInMin) {
        this.timeInMin = timeInMin;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public JSONObject getApiResponse() {
        return apiResponse;
    }

    public void setApiResponse(JSONObject apiResponse) {
        this.apiResponse = apiResponse;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public void setPolyLineOptions(PolylineOptions polyLineOptions) {
        this.polyLineOptions = polyLineOptions;
    }

    public PolylineOptions getPolyLineOptions() {
        return polyLineOptions;
    }
}
