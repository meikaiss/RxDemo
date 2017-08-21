package com.mike.rxdemo;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HTTP;

/**
 * Created by meikai on 17/8/15.
 */
public interface WeatherApi2 {

    //    @GET("weather")
    @HTTP(method = "GET", path = "weather", hasBody = false)
    Call<MyResponse<Weather>> getWeather();



    @HTTP(method = "GET", path = "weather", hasBody = false)
    Observable<MyResponse<Weather>> getWeather2();

}
