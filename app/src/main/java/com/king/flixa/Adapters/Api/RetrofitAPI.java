package com.king.flixa.Adapters.Api;

import com.king.flixa.Adapters.DataModal;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RetrofitAPI {

    @Headers({
            "x-platform-code: web",
            "x-country-code: IN"
    })
    @POST("users")
    Call<DataModal> createPost(@Body DataModal dataModal);
}

