package com.demo.jp.foursquaredemo.backend;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Jari Pulkkinen on 30.4.2015.
 *
 * This is the connection for Venue search requests.
 *
 */
public class VenueSearchConnection extends BackendConnectionBase {

    private static final String SEARCH_URL = "https://api.foursquare.com/v2/venues/search";

    @Override
    protected String getUrl() {
        return SEARCH_URL;
    }

}
