package com.demo.jp.foursquaredemo.backend;

import android.os.AsyncTask;
import android.util.Log;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Jari Pulkkinen on 30.4.2015.
 *
 * Executes a backend request asynchronously.
 */
public abstract class BackendRequest {

    private static final String TAG = BackendRequest.class.getSimpleName();

    /** The response from the backend. */
    protected BackendResponse mResponse;

    /** Currently executing request. */
    private BackendRequestTask mRequestTask;

    /** Callback for the request result. */
    private BackendRequestObserver mObserver;

    /** The connection to be used for the request. */
    private BackendConnection mConnection;

    /** Flag to indicate if the request should be cancelled. */
    private boolean mIsCancelled;

    /**
     * Execute the request. Execution is done asynchronously and the pObserver is called upon completion.
     * @param pConnection the connection used to send the request.
     * @param pObserver the callback for the result.
     */
    public void execute(final BackendConnection pConnection, final BackendRequestObserver pObserver) {
        Assert.assertNull("Execute already called.", mRequestTask);
        Assert.assertNotNull("Connection is null.", pConnection);
        mConnection = pConnection;
        mObserver = pObserver;
        mRequestTask = new BackendRequestTask();
        mRequestTask.execute();
    }

    /**
     * Cancel the request.
     */
    public void cancel() {
        mIsCancelled = true;
    }

    /**
     * AsyncTask for executing the request in background thread.
     */
    private class BackendRequestTask extends AsyncTask<Void, Void, BackendResponse> {
        @Override
        protected BackendResponse doInBackground(final Void... params) {

            // The response object will contain the response code and JSON from the backend
            // or alternatively the exception and error message if something goes wrong.
            BackendResponse response = new BackendResponse();

            try {

                // Connect and read the response.
                int responseCode = mConnection.connect(getRequestParams());
                response.setResponseCode(responseCode);
                response.setResponse(readResponseJSON(mConnection.getInputStream()));
                mConnection.disconnect();

                // Set the cancelled flag in the response if request was cancelled.
                if(mIsCancelled) {
                    response.setCancelled();
                }

            } catch (IOException pException) {
                response.setError(pException, "Unable to connect.");
            } catch (JSONException pException) {
                response.setError(pException, "Unable to parse response JSON.");
            }

            return response;

        }

        @Override
        protected void onPostExecute(final BackendResponse pResponse) {
            mResponse = pResponse;
            // Notify the observer that things are done.
            if(mObserver != null) {
                mObserver.onRequestCompleted(mResponse);
            }
        }
    }

    /**
     * Creates a JSONObject from the InputStream provided by the connection.
     * @param pInputStream
     * @return
     * @throws IOException
     * @throws JSONException
     */
    private JSONObject readResponseJSON(final InputStream pInputStream) throws IOException, JSONException {
        InputStreamReader isReader = new InputStreamReader(pInputStream);
        StringBuilder builder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(isReader);

        String line = bufferedReader.readLine();
        while(line != null) {

            if(mIsCancelled) {
                Log.d(TAG, "readResponseJSON cancelled.");
                return null;
            }

            builder.append(line);
            line = bufferedReader.readLine();
        }

        return new JSONObject(builder.toString());

    }

    /**
     * Get the additional parameters for this request.
     * @return
     */
    protected abstract String getRequestParams();

}
