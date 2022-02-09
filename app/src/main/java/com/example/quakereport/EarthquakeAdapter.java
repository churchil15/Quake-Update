package com.example.quakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    private static final String LOCATION_SEPARATOR = " of ";

    public EarthquakeAdapter(Activity context, ArrayList<Earthquake> earthquakes){
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, earthquakes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        // Check if there is an existing list item view (convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list_item, parent, false);
        }

        // Find the earthquake at the given position in the list of earthquakes
        final Earthquake currentEarthquake = getItem(position);

        // Find the TextView with view ID magnitude
        TextView magnitudeView = (TextView) listItemView.findViewById(R.id.magnitude);


        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);


        // Format the magnitude to show 1 decimal place
        String formattedMagnitude = formatMagnitude(currentEarthquake.getMagnitude());
        // Display the magnitude of the current earthquake in that TextView
        magnitudeView.setText(formattedMagnitude);

        // We are splitting the location into two parts--
        // Location Offset and Primary Location
        String originalLocation = currentEarthquake.getPlace();
        String locationOffset;
        String primaryLocation;

        // Checking if the location has an location offset or not,
        // and then adjusting the primary and offset locations accordingly
        if (originalLocation.contains(LOCATION_SEPARATOR)){
            String[] parts = originalLocation.split(LOCATION_SEPARATOR);
            locationOffset = parts[0] + LOCATION_SEPARATOR;
            primaryLocation = parts[1];
        }
        else{
            locationOffset = getContext().getString(R.string.near_the);
            primaryLocation = originalLocation;
        }

        TextView locationOffsetView = (TextView) listItemView.findViewById(R.id.location_offset);
        locationOffsetView.setText(locationOffset);

        TextView primaryLocationView = (TextView) listItemView.findViewById(R.id.primary_location);
        primaryLocationView.setText(primaryLocation);

        // Create a new Date object from the time in milliseconds of the earthquake
        Date dateObject = new Date(currentEarthquake.getTimeInMilliseconds());

        // Find the TextView with the ID date
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        // Format the date string
        String formattedDate = formatDate(dateObject);
        // Display the date of the current earthquake in that TextView
        dateView.setText(formattedDate);

        // Find the TextView with the ID time
        TextView timeView = (TextView) listItemView.findViewById(R.id.time);
        // Format the time string
        String formattedTime = formatTime(dateObject);
        // Display the time of the current earthquake in that TextView
        timeView.setText(formattedTime);


        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return  listItemView;
    }

    private int getMagnitudeColor(float magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor){
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }

    private String formatMagnitude(float magnitude) {
        DecimalFormat decimalFormatter = new DecimalFormat("0.0");
        return decimalFormatter.format(magnitude);
    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("LLL dd, yyyy");
        return  dateFormatter.format(dateObject);
    }

    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");
        return  timeFormatter.format(dateObject);
    }


}
