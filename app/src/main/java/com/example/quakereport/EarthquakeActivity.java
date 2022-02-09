package com.example.quakereport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    // TextView that is displayed when the list is empty
    private TextView emptyStateTextView;


    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    // URL for earthquake data from the USGS dataset
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";

    // Adapter for the list of earthquakes
    private EarthquakeAdapter adapter;


    // Constant value for the earthquake loader ID. We can choose any integer.
    // This really only comes into play if you're using multiple loaders.
    private static final int EARTHQUAKE_LOADER_ID = 1;

    @NonNull
    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String maxMagnitude = sharedPrefs.getString(
                getString(R.string.settings_max_magnitude_key),
                getString(R.string.settings_max_magnitude_default));

        String itemNumber = sharedPrefs.getString(
                getString(R.string.settings_item_number_key),
                getString(R.string.settings_item_number_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", itemNumber);
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("maxmag", maxMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        // Create a new loader for the given URL
        return new EarthquakeLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {

        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Clear the adapter of previous earthquake data
        adapter.clear();

        // Set empty state text to display "No earthquakes found."
        emptyStateTextView.setText(R.string.no_earthquakes);





        // For Checking Internet Connectivity and displaying the message accordingly -->
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            emptyStateTextView.setText(R.string.no_earthquakes);

        } else {

            emptyStateTextView.setText(R.string.no_internet_connection);

        }

        loadingIndicator.setVisibility(View.GONE);





        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (earthquakes != null && !earthquakes.isEmpty()) {
            adapter.addAll(earthquakes);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        // Loader reset, so we can clear out our existing data.
        adapter.clear();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Find a reference to the ListView in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        emptyStateTextView = (TextView) findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(emptyStateTextView);

        // Create a new adapter that takes an empty list of earthquakes as input
        adapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

        // Set the adapter on the ListView
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Find the current earthquake that was clicked on
                Earthquake currentEarthquake = adapter.getItem(position);

                // Convert the String URL into URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });


        // This was the code for AsyncTask without the usage of loader
//        // Start the AsyncTask to fetch the earthquake data
//        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
//        task.execute(USGS_REQUEST_URL);



        // LoaderManager -->
        // Get a reference to the LoaderManager, in order to interact with loaders.
        LoaderManager loaderManager = getSupportLoaderManager();

        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);


    }




//    /**
//     * {@link AsyncTask} to perform the network request on a background thread, and then
//     * update the UI with the list of earthquakes in the response.
//     *
//     * AsyncTask has three generic parameters: the input type, a type used for progress updates, and
//     * an output type. Our task will take a String URL, and return an Earthquake. We won't do
//     * progress updates, so the second generic is just Void.
//     *
//     * We'll only override two of the methods of AsyncTask: doInBackground() and onPostExecute().
//     * The doInBackground() method runs on a background thread, so it can run long-running code
//     * (like network activity), without interfering with the responsiveness of the app.
//     * Then onPostExecute() is passed the result of doInBackground() method, but runs on the
//     * UI thread, so it can use the produced data to update the UI.
//     */
//    private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<Earthquake>> {
//
//        // This method runs on a background thread and performs the network request.
//        // We should not update the UI from a background thread, so we return a list of
//        // Earthquake(s) as the result.
//        @Override
//        protected List<Earthquake> doInBackground(String... urls) {
//            // Don't perform the request if there are no URLs, or the first URL is null.
//            if (urls.length < 1 || urls[0] == null){
//                return null;
//            }
//            List<Earthquake> result = QueryUtils.fetchEarthquakeData(urls[0]);
//            return result;
//        }
//
//
//        // This method runs on the main UI thread after the background work has been
//        // completed. This method receives as input, the return value from the doInBackground()
//        // method. First we clear out the adapter, to get rid of earthquake data from a previous
//        // query to USGS. Then we update the adapter with the new list of earthquakes,
//        // which will trigger the ListView to re-populate its list items.
//
//        @Override
//        protected void onPostExecute(List<Earthquake> data) {
//            // Clear the adapter of previous earthquake data
//            adapter.clear();
//
//            // If there is a valid list Earthquake(s), then add them to the adapter's
//            // data set. This will trigger the ListView to updated.
//            if (data != null && !data.isEmpty()){
//                adapter.addAll(data);
//            }
//        }
//    }

}