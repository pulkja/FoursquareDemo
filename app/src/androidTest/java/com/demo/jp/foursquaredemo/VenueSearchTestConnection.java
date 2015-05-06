package com.demo.jp.foursquaredemo;

import android.annotation.TargetApi;
import android.os.Build;

import com.demo.jp.foursquaredemo.backend.BackendConnection;
import com.demo.jp.foursquaredemo.backend.VenueSearchRequest;

import junit.framework.Assert;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by Jari Pulkkinen on 6.5.2015.
 *
 * This is an mock implementation of BackendConnection interface for testing the BackedRequest functionality.
 * Behavior of this "connection" can be changed using the Behavior parameter.
 *
 */
public class VenueSearchTestConnection implements BackendConnection {

    public enum Behavior {
        EMPTY_RESPONSE, UNABLE_TO_CONNECT, SUCCESSFUL
    }

    private Behavior mBehavior;

    public VenueSearchTestConnection(Behavior pBehavior) {
        mBehavior = pBehavior;
    }

    @Override
    public int connect(String pRequestParams) throws IOException {

        switch (mBehavior) {
            case UNABLE_TO_CONNECT:
                throw new IOException();

            default:

                // Verify parameters
                StringBuilder builder = new StringBuilder();
                builder.append("&ll=")
                        .append(TestData.VENUE_SEARCH_LATIUDE).append(",").append(TestData.VENUE_SEARCH_LONGITUDE)
                        .append("&query=")
                        .append(TestData.VENUE_SEARCH_QUERY);

                Assert.assertEquals("Parameters not correctly set.", builder.toString(), pRequestParams);

                return 200;

        }
    }

    @Override
    public void disconnect() {

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public InputStream getInputStream() throws IOException {

        switch (mBehavior) {
            case EMPTY_RESPONSE:
                return new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8));
            case SUCCESSFUL:
                return new ByteArrayInputStream(TestData.VENUES_RESPONSE_JSON.getBytes(StandardCharsets.UTF_8));
            default:
                Assert.fail("No behavior set.");
                return null;
        }
    }

}
