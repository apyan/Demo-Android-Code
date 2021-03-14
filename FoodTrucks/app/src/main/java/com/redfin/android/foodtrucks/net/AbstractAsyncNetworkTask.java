package com.redfin.android.foodtrucks.net;

import android.net.Uri;
import android.os.AsyncTask;

import java.lang.reflect.Type;

/**
 * Abstract class for handling network requests off of the main thread. This class needs a URI to hit,
 * the callback for the response, and the type to deserialize to. With that, it will run a network
 * task off of the main thread, deserialize the response and call the Callback with the response.
 *
 * @see GetFoodTrucksTask
 * @see ApiClient
 */
public class AbstractAsyncNetworkTask<T> extends AsyncTask<String, Void, T> {
    private Uri uri;
    private Callback<T> callback;

    private Exception exception;
    private Type deserializationType;

    /**
     * Constructor for an AbstractAsyncNetworkTask. This class needs a URI to hit,
     * the callback for the response, and the type to deserialize to. With that, it will run a network
     * task off of the main thread, deserialize the response and call the Callback with the response.
     * @param uri to for the network call
     * @param callback to call with the response
     * @param deserializationType to deserialize the payload with
     */
    public AbstractAsyncNetworkTask(Uri uri, Callback<T> callback, Type deserializationType) {
        super();
        this.uri = uri;
        this.callback = callback;
        this.deserializationType = deserializationType;
    }

    @Override
    protected T doInBackground(String... strings) {
        try {
            return ApiClient
                    .get()
                    .getPayload(uri, deserializationType);
        }
        catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(T result) {
        super.onPostExecute(result);

        if (this.callback != null) {
            this.callback.handleCallback(result, exception);
        }
    }
}
