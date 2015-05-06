package com.demo.jp.foursquaredemo.backend;

/**
 * Created by Jari Pulkkinen on 5.5.2015.
 *
 * Helper class for executing backend requests.
 *
 */
public class RequestFactory {

    public static BackendRequest executeVenueSearchRequest(final double pLongitude, final double pLatitude, final String pQuery, final BackendRequestObserver pObserver) {
        BackendRequest request = new VenueSearchRequest(pLongitude, pLatitude, pQuery);
        request.execute(new VenueSearchConnection(), pObserver);
        return request;
    }

}
