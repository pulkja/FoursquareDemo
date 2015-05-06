package com.demo.jp.foursquaredemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.demo.jp.foursquaredemo.data.Venue;
import com.demo.jp.foursquaredemo.data.Venues;

/**
 * Created by Jari Pulkkinen on 30.4.2015.
  *
 * This is the adapter for providing views for the shop list.
 * Also provides the map as first item of the list.
 *
 */
public class VenueListAdapter extends BaseAdapter {

    /** Reference to the venue data for getting content for the view */
    private Venues mVenues;

    /**
     * We don't want to recreate the map view every time so we keep a reference here.
     */
    private View mMapView;

    public VenueListAdapter(final Venues pShopData) {
        mVenues = pShopData;
    }

    @Override
    public int getCount() {
        return mVenues.getCount();
    }

    @Override
    public Object getItem(final int pPosition) {
        return null;
    }

    @Override
    public long getItemId(final int pPosition) {
        return 0;
    }

    @Override
    public View getView(final int pPosition, final View pConvertView, final ViewGroup pParent) {

        return getVenueView(pPosition, pParent);

    }

    private View getVenueView(final int pPosition, final ViewGroup pParent) {

        final Venue venue = mVenues.getVenue(pPosition);

        LayoutInflater inflater = (LayoutInflater)pParent.getContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        View shopItemView = inflater.inflate(R.layout.venue_list_item, pParent, false);
        TextView venueNameText = (TextView) shopItemView.findViewById(R.id.venue_name_text);
        TextView venueDistanceText = (TextView) shopItemView.findViewById(R.id.venue_distance_text);
        TextView venueAddressText = (TextView) shopItemView.findViewById(R.id.venue_address_text);

        venueNameText.setText(venue.getName());
        venueDistanceText.setText(String.format(pParent.getContext().getString(R.string.x_meters), String.valueOf(venue.getDistance())));

        if(venue.getAddress() == null || venue.getAddress().isEmpty()) {
            venueAddressText.setText(pParent.getContext().getString(R.string.unknown_address));
        } else {
            venueAddressText.setText(venue.getAddress());
        }

        return shopItemView;
    }

}
