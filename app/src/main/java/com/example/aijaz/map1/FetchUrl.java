package com.example.aijaz.map1;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

/**
 * Created by aijaz on 3/19/17.
 */

// Fetches data from url passed
public class FetchUrl extends AsyncTask<Object, String, String> {
    GoogleMap mMap;
    String url;
    NearByTransit nearByTransit;
    @Override
    protected String doInBackground(Object... params) {

        // For storing data from web service
        String data = "";

        try {
            mMap = (GoogleMap) params[0];
            url = (String) params[1];
            nearByTransit = (NearByTransit) params[2];
            // Fetching the data from web service
            DownloadUrl downloadUrl = new DownloadUrl();
//            data = downloadUrl.readUrl(url[0]);
            data = downloadUrl.readUrl(url);
            Log.d("Background Task data", data.toString());
        } catch (Exception e) {
            Log.d("Background Task", e.toString());
        }
        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);


        Object[] DataTransfer = new Object[3];
        DataTransfer[0] = mMap;
        DataTransfer[1] = result;
        DataTransfer[2] = nearByTransit;

//        ParserTime parserTime = new ParserTime();
//
//        parserTime.execute(DataTransfer);
        int c = 0;
        if(!result.contains("error_message")){
            ParserTask parserTask = new ParserTask();


            // Invokes the thread for parsing the JSON data
            parserTask.execute(DataTransfer);
            Log.d("Fetch Url Success", "number for" + nearByTransit.getPos().toString());
        } else {

            Log.d("Fetch Url Error", "number for" + nearByTransit.getPos().toString());
        }


    }
}
