package com.redfin.android.foodtrucks.model;

import java.io.Serializable;

/**
 * Class representing a LatLng returned from the sfgov food truck data source. Food Trucks store
 * their coordinates in this format.
 *
 * @see FoodTruck
 */

public class Point implements Serializable{
    private String type;
    private double[] coordinates;

    public double getLatitude() {
        return coordinates[1];
    }

    public double getLongitude() {
        return coordinates[0];
    }
}
