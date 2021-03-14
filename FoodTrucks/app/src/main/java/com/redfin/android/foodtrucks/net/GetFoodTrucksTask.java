package com.redfin.android.foodtrucks.net;

import android.net.Uri;
import android.os.AsyncTask;

import com.google.gson.reflect.TypeToken;
import com.redfin.android.foodtrucks.model.FoodTruck;

import java.util.ArrayList;
import java.util.List;

/**
 * A network task that will run a network call on a background thread and call a {@link Callback}
 * instance back on the main thread. The callback instance will receive a {@link List<FoodTruck>}
 * containing the response from the network request.
 *
 * @see Callback
 * @see FoodTruck
 */
public class GetFoodTrucksTask extends AbstractAsyncNetworkTask<List<FoodTruck>> {

    /**
     * Constructor for an AbstractAsyncNetworkTask. This class needs a URI to hit,
     * the Callback for the response. With that, it will run a network task off of the main thread,
     * deserialize the response to a {@link List<FoodTruck> }and call the Callback with the response.
     * @param uri to for the network call
     * @param callback to call with the response
     */
    public GetFoodTrucksTask(Uri uri, Callback<List<FoodTruck>> callback) {
        super(uri, callback, new TypeToken<ArrayList<FoodTruck>>(){}.getType());
    }
}
