package com.mike.rxdemo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitActivity extends AppCompatActivity {

    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson4);

        tvResult = (TextView) findViewById(R.id.tv_result);
    }

    public void getWeather(View view) {

        final Gson gson = new GsonBuilder().create();

//        创建Retrofit实例时需要通过Retrofit.Builder,并调用baseUrl方法设置URL。
//        注： Retrofit2 的baseUlr 必须以 /（斜线） 结束，不然会抛出一个IllegalArgumentException,
//        所以如果你看到别的教程没有以 / 结束，那么多半是直接从Retrofit 1.X 照搬过来的。
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl("http://weixin.jirengu.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        WeatherApi weatherService = retrofit.create(WeatherApi.class);

        Call<MyResponse<Weather>> call = weatherService.getWeather();

        call.enqueue(new Callback<MyResponse<Weather>>() {
            @Override
            public void onResponse(Call<MyResponse<Weather>> call, Response<MyResponse<Weather>> response) {

                tvResult.setText(gson.toJson(response.body()));

                Log.d("http", gson.toJson(response.body()));
            }

            @Override
            public void onFailure(Call<MyResponse<Weather>> call, Throwable t) {
                Log.d("http", t.getMessage());
            }
        });

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                //Emitter是发射器的意思，那就很好猜了，这个就是用来发出事件的，它可以发出三种类型的事件
                //通过调用emitter的onNext(T value)、onComplete()和onError(Throwable error)就可以分别发出next事件、complete事件和error事件。

                e.onNext("1");
                e.onNext("2");
                e.onNext("3");
                e.onComplete();

            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String value) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }


}
