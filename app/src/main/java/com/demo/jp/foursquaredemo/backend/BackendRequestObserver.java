package com.demo.jp.foursquaredemo.backend;

/**
 * Created by Jari Pulkkinen on 30.4.2015.
 */
public interface BackendRequestObserver {

    public void onRequestCompleted(final BackendResponse pResponse);

}
