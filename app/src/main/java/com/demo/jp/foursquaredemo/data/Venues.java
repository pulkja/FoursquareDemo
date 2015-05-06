package com.demo.jp.foursquaredemo.data;

import android.content.Context;
import android.util.Log;
import com.demo.jp.foursquaredemo.backend.BackendRequest;
import com.demo.jp.foursquaredemo.backend.BackendRequestObserver;
import com.demo.jp.foursquaredemo.backend.BackendResponse;
import com.demo.jp.foursquaredemo.backend.RequestFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Jari Pulkkinen on 30.4.2015.
 *
 * Holds all venue related information. Handles loading also.
 * This is just imaginary data that the shops might provide. In real case these would be retrieved from a backend.
 *
 */
public class Venues {

    private static final String TAG = Venues.class.getSimpleName();

    /** List of venues */
    private List<Venue> mVenueList = new ArrayList<Venue>();

    /** Observer to be notified when the venues list is updated. */
    private VenuesUpdateObserver mUpdateObserver;

    /** Reference to currently executing request so it can be cancelled when necessary. */
    private BackendRequest mSearchRequest;

    /** Currently used search query */
    private String mCurrentQuery;

    /**
     * Get the current status of fetching.
     * @return
     */
    public boolean isFetching() {
        return mSearchRequest != null;
    }

    /**
     * Get the currently used search query.
     * @return
     */
    public String getCurrentQuery() {
        return mCurrentQuery;
    }

    /**
     * Observer interface for venue update events.
     */
    public interface VenuesUpdateObserver {
        public void onVenuesUpdated();
        public void onVenueUpdateFailed();
    }

    public Venues(final VenuesUpdateObserver pObserver) {
        mUpdateObserver = pObserver;
    }

    /**
     * Get a {@link com.demo.jp.foursquaredemo.data.Venue} at denoted position.
     * @param pPosition
     * @return
     */
    public Venue getVenue(final int pPosition) {
        return mVenueList.get(pPosition);
    }

    /**
     * Get the count of venues.
     * @return
     */
    public int getCount() {
        if(mVenueList != null) {
            return mVenueList.size();
        } else {
            return 0;
        }
    }

    /**
     * Update a list of Foursquare venues around denoted longitude and latitude using the denoted search query string.
     * @param pLongitude
     * @param pLatitude
     * @param pQuery
     */
    public void fetchData(final double pLongitude, final double pLatitude, final String pQuery) {

        Log.d(TAG, "fetchData");

        if(mSearchRequest != null) {
            mSearchRequest.cancel();
        }

        mCurrentQuery = pQuery;

        mSearchRequest = RequestFactory.executeVenueSearchRequest(pLongitude, pLatitude, pQuery, new BackendRequestObserver() {
            @Override
            public void onRequestCompleted(BackendResponse pResponse) {
                Log.d(TAG, "fetchData: onRequestCompleted");

                mSearchRequest = null;

                if(!pResponse.isCancelled()) {
                    if(pResponse.isSuccessful()) {
                        Log.d(TAG, "fetchData: onRequestCompleted success " + pResponse.getResponseCode());
                        mVenueList = VenueJSONParser.parseVenuesFromJSON(pResponse.getResponseJSON());
                        sortByDistance(mVenueList);
                        if(mUpdateObserver != null) {
                            mUpdateObserver.onVenuesUpdated();
                        }
                    } else {
                        if(mUpdateObserver != null) {
                            mUpdateObserver.onVenueUpdateFailed();
                        }
                        Log.d(TAG, "fetchData: onRequestCompleted failed " + pResponse.getError());
                    }
                } else {
                    Log.d(TAG, "fetchData: request was cancelled.");
                }
            }
        });

    }

    /**
     * Sorts the venue list by distance.
     * @param pVenueList
     */
    private void sortByDistance(final List<Venue> pVenueList) {

        // Sort the shop list by distances.
        Collections.sort(pVenueList, new Comparator<Venue>() {
            @Override
            public int compare(Venue pLeft, Venue pRight) {
                return pLeft.getDistance() - pRight.getDistance();
            }
        });

    }
}
