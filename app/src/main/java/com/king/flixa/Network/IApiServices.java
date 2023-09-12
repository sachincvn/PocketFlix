package com.king.flixa.Network;

import com.king.flixa.Model.HotstarMovieModels.HotstarMoviModel;
import com.king.flixa.Model.Response.EventResponse;
import com.king.flixa.Model.Response.LiveChannelsResponse;
import com.king.flixa.Model.Response.SliderBannerResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Url;

public interface IApiServices {
    @GET
    Call<EventResponse> getApiRequest(@Url String url);


    @GET
    Call<LiveChannelsResponse> getLiveChannels(@Url String url);

    @GET
    Call<SliderBannerResponse> getBannerItems(@Url String url);


    @Headers({
            "x-platform-code: PCTV",
            "x-country-code: IN"
    })
    @GET
    Call<HotstarMoviModel> getHotstarMovies(@Url String api);

    @GET
    Call<ResponseBody> getStringData(@Url String url);
}
