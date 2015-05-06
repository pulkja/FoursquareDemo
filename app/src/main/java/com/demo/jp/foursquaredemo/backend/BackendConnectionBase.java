package com.demo.jp.foursquaredemo.backend;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Jari Pulkkinen on 5.5.2015.
 *
 * Base class for the BackendConnection implementations.
 */
public abstract class BackendConnectionBase implements BackendConnection {

    /** Client ID and secret must be included in the request URL. Get these by createing app for Foursquare. */
    private static final String CLIENT_ID = "GET_YOUR_OWN!";
    private static final String CLIENT_SECRET = "GET_YOUR_OWN!";

    /** Version of API to be used. */
    private String mAPIVersion = "20130506";

    /** The actual HTTP connection. */
    HttpURLConnection mConnection;

    @Override
    public int connect(final String pRequestParams) throws IOException {

        // Build the URL
        StringBuilder urlBuilder = new StringBuilder(getUrl());
        urlBuilder.append("?client_id=")
                .append(CLIENT_ID)
                .append("&client_secret=")
                .append(CLIENT_SECRET)
                .append("&v=")
                .append(mAPIVersion)
                .append(pRequestParams);

        URL url = new URL(urlBuilder.toString());

        // Connect
        mConnection = (HttpURLConnection) url.openConnection();
        mConnection.setReadTimeout(10000);
        mConnection.setConnectTimeout(15000);
        mConnection.setRequestMethod("GET");
        mConnection.setDoInput(true);
        mConnection.connect();

        return mConnection.getResponseCode();
    }


    @Override
    public void disconnect() {
        if(mConnection != null) {
            mConnection.disconnect();
        }
    }

    /**
     * Get the request URL.
     * @return
     */
    protected abstract String getUrl();

    @Override
    public InputStream getInputStream() throws IOException {
        return mConnection.getInputStream();
    }

}
