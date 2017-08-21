package com.mike.rxdemo;

import retrofit2.Call;
import retrofit2.http.HTTP;

/**
 * Created by meikai on 17/8/15.
 */
public interface WeatherApi {

    //    @GET("weather")
    @HTTP(method = "GET", path = "weather", hasBody = false)
    Call<MyResponse<Weather>> getWeather();
}
