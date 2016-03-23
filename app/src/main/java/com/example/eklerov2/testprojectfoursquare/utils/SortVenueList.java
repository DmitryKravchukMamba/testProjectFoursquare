package com.example.eklerov2.testprojectfoursquare.utils;

import com.example.eklerov2.testprojectfoursquare.models.venuemodel.Item_;
import com.example.eklerov2.testprojectfoursquare.models.venuemodel.Venue;

import java.util.Comparator;

/**
 * Created by Evgen on 19.03.16.
 */

public class SortVenueList implements Comparator<Item_> {

    @Override
    public int compare(Item_ e1, Item_ e2) {
        if(e1.getVenue().getLocation().getDistance() > e2.getVenue().getLocation().getDistance()){
            return 1;
        } else {
            return -1;
        }
    }
}