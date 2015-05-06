package com.demo.jp.foursquaredemo.data;

import android.graphics.Point;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Jari Pulkkinen on 30.4.2015.
 *
 * Holds information on a venue.
 *
 */
public class Venue {

    /** Name of this venue */
    private String mName;

    /** Address of the venue */
    private String mAddress;

    /** Distance between venue and user as meters */
    private int mDistance;

    public Venue(final String pName, final String pAddress, final int pDistance) {
        mName = pName;
        mAddress = pAddress;
        mDistance = pDistance;
    }

    /**
     * Get the name of the venue.
     * @return
     */
    public String getName() {
        return mName;
    }

    /**
     * Get the address of the venue.
     * @return
     */
    public String getAddress() {
        return mAddress;
    }

    /**
     * Get the distance between venue and user as meters.
     * @return
     */
    public int getDistance() {
        return mDistance;
    }

}
