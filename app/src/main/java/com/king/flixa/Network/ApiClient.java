package com.king.flixa.Network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit;
    public static Retrofit getRetrofitInstance(String apiBaseUrl) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(apiBaseUrl).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}