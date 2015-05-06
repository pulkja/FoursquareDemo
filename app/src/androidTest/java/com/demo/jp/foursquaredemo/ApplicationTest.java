package com.demo.jp.foursquaredemo;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.demo.jp.foursquaredemo.backend.BackendConnection;
import com.demo.jp.foursquaredemo.backend.BackendRequest;
import com.demo.jp.foursquaredemo.backend.BackendRequestObserver;
import com.demo.jp.foursquaredemo.backend.BackendResponse;
import com.demo.jp.foursquaredemo.backend.VenueSearchConnection;
import com.demo.jp.foursquaredemo.backend.VenueSearchRequest;
import com.demo.jp.foursquaredemo.data.Venue;
import com.demo.jp.foursquaredemo.data.VenueJSONParser;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    private BackendResponse mLastResponse;

    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        createApplication();
    }

    public void testVenueSearchRequestEmptyResponse() {

        executeTestRequest(VenueSearchTestConnection.Behavior.EMPTY_RESPONSE);

        // Connection was ok, so response code should be 200
        Assert.assertEquals("Wrong response code", 200, mLastResponse.getResponseCode());

        // The response must not be successful
        Assert.assertFalse(mLastResponse.isSuccessful());

        // Exception is JSONException
        Assert.assertTrue("Error must be JSONException", mLastResponse.getError() instanceof JSONException);
        Assert.assertNotNull("Error message must be set", mLastResponse.getErrorMessage());

    }

    public void testVenueSearchRequestUnableToConnect() {

        executeTestRequest(VenueSearchTestConnection.Behavior.UNABLE_TO_CONNECT);

        // The response must not be successful
        Assert.assertFalse(mLastResponse.isSuccessful());

        // Exception is IOException
        Assert.assertTrue("Error must be IOException", mLastResponse.getError() instanceof IOException);
        Assert.assertNotNull("Error message must be set", mLastResponse.getErrorMessage());

    }

    public void testVenueSearchRequestSuccessful() {

        executeTestRequest(VenueSearchTestConnection.Behavior.SUCCESSFUL);

        // The response must be successful
        Assert.assertTrue(mLastResponse.isSuccessful());

        // No errors are set
        Assert.assertNull(mLastResponse.getError());
        Assert.assertNull(mLastResponse.getErrorMessage());

        // Parse the response
        JSONObject venueJSON = mLastResponse.getResponseJSON();
        List<Venue> venues = VenueJSONParser.parseVenuesFromJSON(venueJSON);

        // Check parsed venues
        Assert.assertEquals(5, venues.size());

        // Check first two venues.
        Assert.assertEquals("Sushi Gallery", venues.get(0).getName());
        Assert.assertEquals(612, venues.get(0).getDistance());
        Assert.assertEquals("Brooklyn, NY, United States", venues.get(0).getAddress());

        Assert.assertEquals("sushi garden", venues.get(1).getName());
        Assert.assertEquals(1012, venues.get(1).getDistance());
        Assert.assertEquals("165 Joralemon St, Brooklyn, NY, United States", venues.get(1).getAddress());

    }

    private void executeTestRequest(VenueSearchTestConnection.Behavior pBehavior) {

        final CountDownLatch latch = new CountDownLatch(1);

        mLastResponse = null;

        BackendRequest request = new VenueSearchRequest(TestData.VENUE_SEARCH_LONGITUDE, TestData.VENUE_SEARCH_LATIUDE, TestData.VENUE_SEARCH_QUERY);
        request.execute(new VenueSearchTestConnection(pBehavior), new BackendRequestObserver() {
            @Override
            public void onRequestCompleted(BackendResponse pResponse) {

                mLastResponse = pResponse;

                latch.countDown();

            }
        });

        try {
            latch.await(5000, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(mLastResponse);

    }

}