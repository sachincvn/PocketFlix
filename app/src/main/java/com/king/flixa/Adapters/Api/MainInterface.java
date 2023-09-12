package com.king.flixa.Adapters.Api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;


public interface MainInterface {
    @Headers({
            "x-platform-code: web",
            "x-country-code: IN"
    })
     @GET("items")
    Call<String> STRING_CALL(
            @Query("tao") int page,
            @Query("tas") int limit
    );

}