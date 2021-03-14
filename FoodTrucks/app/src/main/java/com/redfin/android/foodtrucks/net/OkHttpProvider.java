package com.redfin.android.foodtrucks.net;

import okhttp3.OkHttpClient;

/**
 * Provider for a singleton OkHttpClient instance. For now this uses a default client configuration,
 * but more configurations could be added here.
 */

public class OkHttpProvider {

    private static OkHttpClient instance;

    public static OkHttpClient get() {
        if (instance == null) {
            instance = new OkHttpClient.Builder()
                    .build();
        }

        return instance;
    }
}
