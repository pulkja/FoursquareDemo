package com.demo.jp.foursquaredemo.data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jari Pulkkinen on 4.5.2015.
 *
 * Used to parse Venue objects out of JSONObject received from the Venue search requests.
 *
 */
public class VenueJSONParser {

    /** The necessary JSON keys  */
    private static final String KEY_RESPONSE = "response";
    private static final String KEY_VENUES = "venues";
    private static final String KEY_NAME = "name";
    private static final String KEY_ADDRESS = "formattedAddress";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_DISTANCE = "distance";

    private static final String TAG = VenueJSONParser.class.getSimpleName();

    public static List<Venue> parseVenuesFromJSON(final JSONObject pVenuesJSON) {

        List<Venue> venueList = new ArrayList<Venue>();

        if(pVenuesJSON == null) {
            return venueList;
        }

        try {
            // Get the response object
            JSONObject response = pVenuesJSON.getJSONObject(KEY_RESPONSE);

            // Get the venues array
            JSONArray venues = response.getJSONArray(KEY_VENUES);

            // Go through the venus array and parse venue names and distances.
            for(int i = 0; i < venues.length(); i++) {
                JSONObject venue = venues.getJSONObject(i);

                // Get the name
                String name = venue.getString(KEY_NAME);

                // Address and distance is part of the location object.
                JSONObject location = venue.getJSONObject(KEY_LOCATION);

                // Get the address
                JSONArray formattedAddress = location.optJSONArray(KEY_ADDRESS);
                StringBuilder addressBuilder = new StringBuilder();
                if(formattedAddress != null) {
                    for(int addressIndex = 0; addressIndex < formattedAddress.length(); addressIndex++) {
                        if(addressIndex > 0) {
                            addressBuilder.append(", ");
                        }
                        addressBuilder.append(formattedAddress.get(addressIndex));
                    }
                }

                // Get the distance
                int distance = location.getInt(KEY_DISTANCE);
                venueList.add(new Venue(name, addressBuilder.toString(), distance));
            }

        } catch (JSONException pException) {
            Log.e(TAG, "Failed to parse venues from JSON: " + pException);
        }

        return venueList;
    }

}
