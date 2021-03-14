package com.redfin.android.foodtrucks.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.redfin.android.foodtrucks.R;
import com.redfin.android.foodtrucks.adapter.FoodTruckAdapter;
import com.redfin.android.foodtrucks.model.FoodTruck;
import com.redfin.android.foodtrucks.net.Callback;
import com.redfin.android.foodtrucks.net.GetFoodTrucksTask;
import com.redfin.android.foodtrucks.view.MapCardView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A blank activity that is launched as the main activity in this application. This activity contains
 * a single method {@link #exampleFoodTruckFetch()} that shows an example of how to use our API
 * for getting a list of food truck data.
 *
 * You are welcome to use this Activity for building your UI. You can also feel free to start from
 * scratch and create a new Activity if that is easier for you.
 */
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final String BUNDLE_MAP_VIEW = "map_view";

    private static final int RESULT_NO_ERROR = 0;
    private static final int RESULT_NO_CONNECTION = 1;
    private static final int RESULT_NO_RESULT = 2;

    private static final double SAN_FRANCISCO_LATITUDE = 37.7749;
    private static final double SAN_FRANCISCO_LONGITUDE = -122.4194;
    private static final double CAMERA_PADDING_MARKERS = 0.05;

    private MapView mMapView;
    private GoogleMap mGoogleMap;

    private List<FoodTruck> mFoodTruckList;
    private FoodTruckAdapter mFoodTruckAdapter;
    private MapCardView mMapCardView;
    private boolean isMapView = false;

    private void exampleFoodTruckFetch() {
        // Create an instance of a network task to get food trucks data
        // This will get Food truck data in a JSON format from http://data.sfgov.org/resource/bbb8-hzi6.json
        GetFoodTrucksTask networkTask = new GetFoodTrucksTask(
                Uri.parse("http://data.sfgov.org/resource/bbb8-hzi6.json"),
                new Callback<List<FoodTruck>>() {
            @Override
            public void handleCallback(@Nullable List<FoodTruck> result, @Nullable Exception exception) {
                // This callback will be called with the result from the network task. Result
                // will contain the list of food trucks returned by the endpoint. Result may be
                // null if there is an error in the network request, in that case exception will be nonnull.
                if(result != null) {
                    mFoodTruckList = currentFoodTruckListing(result);
                    mFoodTruckAdapter.setFoodTruckList(mFoodTruckList);
                    resultDisplayed((mFoodTruckList.size() > 0) ? RESULT_NO_ERROR : RESULT_NO_RESULT);
                    setupMapPins();
                } else {
                    resultDisplayed(RESULT_NO_CONNECTION);
                }
            }
        });

        networkTask.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_truck_list);

        mFoodTruckList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.food_truck_list);
        mFoodTruckAdapter = new FoodTruckAdapter(mFoodTruckList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mFoodTruckAdapter);

        mMapCardView = findViewById(R.id.food_truck_card);

        mMapView = findViewById(R.id.food_truck_map);
        mMapView.onCreate((savedInstanceState != null)
                ? savedInstanceState.getBundle(BUNDLE_MAP_VIEW) : null);
        mMapView.getMapAsync(this);

        exampleFoodTruckFetch();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_list_map:
                // Toggle between map view and list view
                isMapView = !isMapView;
                setTitle(isMapView ? getString(R.string.title_activity_maps) : getString(R.string.app_name));
                item.setTitle(isMapView ? getString(R.string.action_list) : getString(R.string.action_map));

                findViewById(R.id.list_container).setVisibility(isMapView ? View.GONE : View.VISIBLE);
                findViewById(R.id.map_container).setVisibility(isMapView ? View.VISIBLE : View.GONE);
                break;
            default:
                break;
        }
        return true;
    }

    private void resultDisplayed(int result) {
        switch (result) {
            case RESULT_NO_ERROR:
                findViewById(R.id.status_text).setVisibility(View.GONE);
                break;
            case RESULT_NO_CONNECTION:
                findViewById(R.id.status_text).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.status_text)).setText(getString(R.string.status_no_connection));
                break;
            case RESULT_NO_RESULT:
                findViewById(R.id.status_text).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.status_text)).setText(getString(R.string.status_no_result));
            default:
                break;
        }
    }

    private ArrayList<FoodTruck> currentFoodTruckListing(List<FoodTruck> result) {
        ArrayList<FoodTruck> filteredFoodTruck = new ArrayList<>();

        // Get day of the week as an integer, ex. Sunday = 0, Monday = 1,...
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int currentTime24 = Integer.parseInt( calendar.get(Calendar.HOUR_OF_DAY) + ""
                + (calendar.get(Calendar.MINUTE) < 10 ? "0" : "") + calendar.get(Calendar.MINUTE));

        for(int index = 0; index < result.size(); index++) {
            // Obtain the start and end time of the food truck
            int startTime24 = Integer.parseInt(result.get(index).getStartTime24().replace(":",""));
            int endTime24 = Integer.parseInt(result.get(index).getEndTime24().replace(":",""));

            if((dayOfWeek == result.get(index).getWeekdayOrder()) &&
                    (startTime24 <= currentTime24) &&
                    (endTime24 >= currentTime24)) {
                filteredFoodTruck.add(result.get(index));
            }
        }

        // Sort alphabetically by food truck name
        Collections.sort(filteredFoodTruck, new Comparator<FoodTruck>() {
            public int compare(FoodTruck ft1, FoodTruck ft2) {
                return ft1.getFoodTruckName().compareTo(ft2.getFoodTruckName());
            }
        });

        return filteredFoodTruck;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setOnMarkerClickListener(this);

        // San Francisco, CA by default
        mGoogleMap.setMinZoomPreference(12);
        LatLng sanFranCA = new LatLng(SAN_FRANCISCO_LATITUDE, SAN_FRANCISCO_LONGITUDE);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(sanFranCA));
    }

    private void setupMapPins() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for(int index = 0; index < mFoodTruckList.size(); index++) {
            Marker foodTruckMarker =  mGoogleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(mFoodTruckList.get(index).getLatitude(), mFoodTruckList.get(index).getLongitude())));
            foodTruckMarker.setTag(index);

            // Used for fitting the marker into the camera
            builder.include(new LatLng(mFoodTruckList.get(index).getLatitude(), mFoodTruckList.get(index).getLongitude()));
        }

        // Fit all the markers into the camera
        if(mFoodTruckList.size() > 0) {
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            int screenHeight = getResources().getDisplayMetrics().heightPixels;
            int screenPadding = (int) (screenWidth * CAMERA_PADDING_MARKERS);
            LatLngBounds bounds = builder.build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, screenWidth, screenHeight, screenPadding);
            mGoogleMap.animateCamera(cameraUpdate);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        mMapCardView.setVisibility(View.VISIBLE);
        int position = (int) marker.getTag();
        mMapCardView.setupTruckInfo(mFoodTruckList.get(position));
        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (outState.getBundle(BUNDLE_MAP_VIEW) == null) {
            outState.putBundle(BUNDLE_MAP_VIEW, new Bundle());
        } else {
            mMapView.onSaveInstanceState(outState.getBundle(BUNDLE_MAP_VIEW));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
