package com.redfin.android.foodtrucks.net;

import android.support.annotation.Nullable;

/**
 * An interface for Callbacks for network Requests. Pass an instance of these callbacks to a network
 * request. The {@link #handleCallback(Object, Exception)} method will be called with the network response.
 *
 * @see AbstractAsyncNetworkTask
 */
public interface Callback<T> {

    /**
     * Method called from GetFoodTrucksTask with the deserialized response from the network request.
     *
     * @param result with the deserialized response from the network request
     * @param e exception if there is an error returned from the network request
     */
    void handleCallback(@Nullable T result, @Nullable Exception e);
}
