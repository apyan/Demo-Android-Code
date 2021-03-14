package com.redfin.android.foodtrucks.model;

import com.google.gson.annotations.SerializedName;
import com.redfin.android.foodtrucks.net.GetFoodTrucksTask;

import java.io.Serializable;

/**
 * A model class representing a Food Truck. Responses from the sfgov data source can be deserialized
 * to a {@link java.util.List<FoodTruck>}.
 *
 * @see GetFoodTrucksTask to run a task that would return instances of this class.
 */
public class FoodTruck implements Serializable {
    @SerializedName("applicant")
    String foodTruckName;

    @SerializedName("location")
    String address;

    @SerializedName("location_2")
    Point latLong;

    @SerializedName("optionaltext")
    String optionalDesc;

    @SerializedName("locationdesc")
    String locationDesc;

    @SerializedName("starttime")
    String startTime;

    @SerializedName("endtime")
    String endTime;

    @SerializedName("dayorder")
    int weekdayOrder;

    @SerializedName("dayofweekstr")
    String weekdayName;

    @SerializedName("start24")
    String startTime24;

    @SerializedName("end24")
    String endTime24;

    public String getFoodTruckName() {
        return foodTruckName;
    }

    public String getAddress() {
        return address;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public double getLatitude() {
        return latLong.getLatitude();
    }

    public double getLongitude() {
        return latLong.getLongitude();
    }

    public String getOptionalDesc() {
        return optionalDesc;
    }

    public int getWeekdayOrder() {
        return weekdayOrder;
    }

    public String getWeekdayName() {
        return weekdayName;
    }

    public String getStartTime24() {
        return startTime24;
    }

    public String getEndTime24() {
        return endTime24;
    }
}
