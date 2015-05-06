package com.demo.jp.foursquaredemo.backend;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Jari Pulkkinen on 30.4.2015.
 *
 * Holds the response code and JSONObject of the response data
 * or if error occurred the Exception and error message.
 */
public class BackendResponse {

    /** The HTTP response code */
    private int mResponseCode;

    /** The error message if something went wrong */
    private String mErrorMessage;

    /** The exception thrown if something went wrong */
    private Exception mException;

    /** The response from backend as JSONObject */
    private JSONObject mResponseJSON;

    /** Indicatates if the request was cancelled */
    private boolean mIsCancelled;

    /**
     * Set the response code from the HTTP connection.
     * @param pResponseCode
     */
    protected void setResponseCode(final int pResponseCode) {
        mResponseCode = pResponseCode;
    }

    /**
     * Get the response code from the HTTP connection.
     * @return
     */
    public int getResponseCode() {
        return mResponseCode;
    }

    /**
     * Used to set the Exception and the message for the response when error occurred.
     * @param pException
     * @param pMessage
     */
    protected void setError(final Exception pException, final String pMessage) {
        mException = pException;
        mErrorMessage = pMessage;
    }

    /**
     * Test if the request was successful.
     * @return
     */
    public boolean isSuccessful() {
        return mException == null && mErrorMessage == null;
    }

    /**
     * Get the Exception that was thrown when error occurred.
     * @return
     */
    public Exception getError() {
        return mException;
    }

    /**
     * Get the explanation on the error that occurred.
     * @return
     */
    public String getErrorMessage() {
        return mErrorMessage;
    }

    /**
     * Set the response JSONObject.
     * @param pResponseJSON
     */
    protected void setResponse(final JSONObject pResponseJSON) {
        mResponseJSON = pResponseJSON;
    }

    /**
     * Get the response as JSONObject.
     * @return
     */
    public JSONObject getResponseJSON() {
        Assert.assertTrue("getResponseJSON() called when request was not successful.", isSuccessful());
        return mResponseJSON;
    }

    /**
     * Sets the cancelled flag.
     */
    protected void setCancelled() {
        mIsCancelled = true;
    }

    /**
     * Tests if the request was cancelled.
     * @return
     */
    public boolean isCancelled() {
        return mIsCancelled;
    }
}
