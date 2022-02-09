package com.example.quakereport;

public class Earthquake {

    private float magnitude;

    private String location;

    // Time of the earthquake
    private long timeInMilliseconds;

    // Website URL of the earthquake
    private String Url;

    public Earthquake(float mag, String pl, long da, String url){
        magnitude = mag;
        location = pl;
        timeInMilliseconds = da;
        Url = url;
    }

    public float getMagnitude() {
        return magnitude;
    }

    public String getPlace() {
        return location;
    }

    public long getTimeInMilliseconds() {
        return timeInMilliseconds;
    }

    public String getUrl() {
        return Url;
    }
}
