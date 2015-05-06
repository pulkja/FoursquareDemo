package com.demo.jp.foursquaredemo.backend;

/**
 * Created by Jari Pulkkinen on 5.5.2015.
 *
 * Backend request for doing venue searching near the specific location.
 *
 */
public class VenueSearchRequest extends BackendRequest {

    /** Latitude of location */
    private String mLatitude;

    /** Longitude of location */
    private String mLongitude;

    /** The query string for searhing */
    private String mQuery;

    /**
     * Constructs new VenueSearchRequest for searching venues mathing the query around the denoted position.
     * @param pLongitude
     * @param pLatitude
     * @param pQuery
     */
    public VenueSearchRequest(final double pLongitude, final double pLatitude, final String pQuery) {
        mLongitude =String.valueOf(pLongitude);
        mLatitude = String.valueOf(pLatitude);
        mQuery = pQuery;
    }

    @Override
    protected String getRequestParams() {
        StringBuilder paramsBuilder = new StringBuilder();
        paramsBuilder.append("&ll=")
                .append(mLatitude).append(",").append(mLongitude)
                .append("&query=")
                .append(mQuery);
        return paramsBuilder.toString();
    }

}
