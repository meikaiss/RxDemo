package com.mike.rxdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void getWeather(View view) {

//        创建Retrofit实例时需要通过Retrofit.Builder,并调用baseUrl方法设置URL。
//        注： Retrofit2 的baseUlr 必须以 /（斜线） 结束，不然会抛出一个IllegalArgumentException,
//        所以如果你看到别的教程没有以 / 结束，那么多半是直接从Retrofit 1.X 照搬过来的。
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://weixin.jirengu.com/").build();

        WeatherService weatherService = retrofit.create(WeatherService.class);

        Call<ResponseBody> call = weatherService.getWeather();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e("onResponse", response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }


}
