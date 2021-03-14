package com.redfin.android.foodtrucks.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.redfin.android.foodtrucks.R;
import com.redfin.android.foodtrucks.model.FoodTruck;

public class MapCardView extends LinearLayout {

    private TextView mTruckName;
    private TextView mTruckAddress;
    private TextView mTruckDescription;
    private TextView mTruckHours;

    public MapCardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.listview_food_truck, this, true);
        init();
    }

    private void init() {
        findViewById(R.id.line_divider).setVisibility(GONE);
        mTruckName = findViewById(R.id.food_truck_name);
        mTruckAddress = findViewById(R.id.food_truck_address);
        mTruckDescription = findViewById(R.id.food_truck_description);
        mTruckHours = findViewById(R.id.food_truck_hours);
    }

    public void setupTruckInfo(FoodTruck foodTruck) {
        mTruckName.setText(foodTruck.getFoodTruckName());
        mTruckAddress.setText(foodTruck.getAddress());
        mTruckDescription.setText(foodTruck.getOptionalDesc());
        mTruckHours.setText(String.format("%s - %s", foodTruck.getStartTime(), foodTruck.getEndTime()));
    }
}
