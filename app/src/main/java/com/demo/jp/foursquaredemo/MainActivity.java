package com.demo.jp.foursquaredemo;

import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.demo.jp.foursquaredemo.data.Venues;

/**
 * Created by Jari Pulkkinen on 30.4.2015.
 *
 **/
 public class MainActivity extends ActionBarActivity implements Venues.VenuesUpdateObserver {

    private static final String TAG = MainActivity.class.getSimpleName();

    /** List for didsplaying the venues */
    private ListView mVenueListView;

    /** Adapter for the venus list */
    private final VenueListAdapter mVenueListAdapter;

    /** Contains the venues data */
    private final Venues mVenues;

    /** Displays info and errors */
    private TextView mInfoTextView;

    public MainActivity() {
        mVenues = new Venues(this);
        mVenueListAdapter = new VenueListAdapter(mVenues);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVenueListView = (ListView) findViewById(R.id.venueListView);
        mVenueListView.setAdapter(mVenueListAdapter);
        mInfoTextView = (TextView) findViewById(R.id.infoText);

    }

    /**
     * Updates the text shown info text.
     */
    private void updateInfoText() {

        if(mVenues.isFetching()) {
            mVenueListView.setVisibility(View.GONE);
            mInfoTextView.setText(getString(R.string.searching_venues));
        } else {
            mVenueListView.setVisibility(View.VISIBLE);
            String query = mVenues.getCurrentQuery();
            if(query != null) {
                mInfoTextView.setText(String.format(getString(R.string.results_for_x), query));
            } else {
                mInfoTextView.setText(getString(R.string.use_search_to_see_nearby_venues));
            }
        }

    }

    /**
     * Checks if network connection is available.
     * @return
     */
    private boolean isConnectedToNetwork() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void fetchData(final String pQuery) {

        if(isConnectedToNetwork()) {
            if (pQuery != null && !pQuery.isEmpty()) {

                // get current position
                LocationManager lm = (LocationManager)getSystemService(LOCATION_SERVICE);
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                // If we couldn't get the GPS location try to get network location.
                if(location == null) {
                    location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }

                if(location == null) {
                    mInfoTextView.setText(getString(R.string.gps_disabled));
                } else {

                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();

                    // fetch data
                    mVenues.fetchData(longitude, latitude, pQuery);
                }
            }

            updateInfoText();

        } else {
            mInfoTextView.setText(getString(R.string.no_network_connection));
            Log.d(TAG, "no network available");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate menu.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView.
        SearchManager searchManager =
                (SearchManager) getSystemService(SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String pQuery) {
                // not interested
                return false;
            }

            @Override
            public boolean onQueryTextChange(String pQuery) {
                fetchData(pQuery);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onVenuesUpdated() {
        mVenueListAdapter.notifyDataSetChanged();
        updateInfoText();
    }

    @Override
    public void onVenueUpdateFailed() {
        mInfoTextView.setText(R.string.venues_update_failed);
    }
}
