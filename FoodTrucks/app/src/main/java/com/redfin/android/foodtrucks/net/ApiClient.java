package com.redfin.android.foodtrucks.net;

import android.net.Uri;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Class responsible for executing an network call and returning a deserialized response.
 *
 * @see OkHttpProvider
 */
public class ApiClient {
    private static ApiClient instance;

    /***
     * Gets a singleton instance of ApiClient
     * @return singleton instance
     */
    public static ApiClient get() {
        if (instance == null) {
            instance = new ApiClient();
        }

        return instance;
    }

    /**
     * Executes a network task to the given URI and returns the deserialized response.
     * @param uri to hit with a network task
     * @param responseType Type of the response. Used to deserialize with GSON
     * @param <T> return type for the response. Should corresponsd with the responseType parameter
     * @return
     * @throws IOException
     */
    public <T> T getPayload(Uri uri, Type responseType) throws IOException {
        Call networkCall = getCall(getRequest(uri));

        Response response = networkCall.execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected response code " + response.toString());
        }

        Gson gson = new Gson();
        String string = response.body().string();
        return gson.fromJson(string, responseType);
    }

    private Request getRequest(Uri uri) {
        Request request = new Request.Builder()
                .url(uri.toString())
                .build();

        return request;
    }

    private Call getCall(Request request) {
        OkHttpClient client = OkHttpProvider.get();
        return client.newCall(request);
    }
}
