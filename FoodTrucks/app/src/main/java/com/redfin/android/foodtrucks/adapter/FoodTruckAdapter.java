package com.redfin.android.foodtrucks.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.redfin.android.foodtrucks.R;
import com.redfin.android.foodtrucks.model.FoodTruck;

import java.util.List;

public class FoodTruckAdapter extends RecyclerView.Adapter<FoodTruckAdapter.ViewHolder> {

    private List<FoodTruck> foodTruckList;

    public FoodTruckAdapter(List<FoodTruck> foodTruckList) {
        this.foodTruckList = foodTruckList;
    }

    public void setFoodTruckList(List<FoodTruck> foodTruckList) {
        this.foodTruckList = foodTruckList;
        notifyDataSetChanged();
    }

    @Override
    public FoodTruckAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.listview_food_truck, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FoodTruckAdapter.ViewHolder holder, int position) {
        final FoodTruck foodTruck = foodTruckList.get(position);
        holder.mTruckName.setText(foodTruck.getFoodTruckName());
        holder.mTruckAddress.setText(foodTruck.getAddress());
        holder.mTruckDescription.setText(foodTruck.getOptionalDesc());
        holder.mTruckHours.setText(String.format("%s - %s", foodTruck.getStartTime(), foodTruck.getEndTime()));
    }

    @Override
    public int getItemCount() {
        return foodTruckList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTruckName;
        public TextView mTruckAddress;
        public TextView mTruckDescription;
        public TextView mTruckHours;

        public ViewHolder(View view) {
            super(view);
            this.mTruckName = view.findViewById(R.id.food_truck_name);
            this.mTruckAddress = view.findViewById(R.id.food_truck_address);
            this.mTruckDescription = view.findViewById(R.id.food_truck_description);
            this.mTruckHours = view.findViewById(R.id.food_truck_hours);
        }
    }
}
