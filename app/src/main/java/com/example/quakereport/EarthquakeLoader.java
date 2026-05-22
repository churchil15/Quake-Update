package com.example.quakereport;

import android.content.Context;

import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    private static final String LOG_TAG = EarthquakeLoader.class.getName();

    private String url;
    public EarthquakeLoader(Context context, String u){
        super(context);
        url = u;
    }

    @Override
    protected void onStartLoading(){
        forceLoad();
    }

    @Override
    public List<Earthquake> loadInBackground() {
        if (url == null){
            return null;
        }

        List<Earthquake> earthquakes = QueryUtils.fetchEarthquakeData((url));
        return earthquakes;
    }
}
