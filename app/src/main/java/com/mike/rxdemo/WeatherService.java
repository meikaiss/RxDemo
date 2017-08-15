package com.mike.rxdemo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by meikai on 17/8/15.
 */
public interface WeatherService {

    @GET("weather")
    Call<ResponseBody> getWeather();
}
