package com.example.quakereport;

import android.content.Context;

import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    private static final String LOG_TAG = EarthquakeLoader.class.getName();

    // Query URL
    private String url;

    // Constructs a new EarthquakeLoader
    public EarthquakeLoader(Context context, String u){
        super(context);
        url = u;
    }

    @Override
    protected void onStartLoading(){
        forceLoad();
    }

    // This is on a background thread.
    @Override
    public List<Earthquake> loadInBackground() {
        if (url == null){
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<Earthquake> earthquakes = QueryUtils.fetchEarthquakeData((url));
        return earthquakes;
    }
}
