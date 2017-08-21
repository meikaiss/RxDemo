package com.mike.rxdemo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by meikai on 17/8/21.
 */
public class RxJavaActivity extends AppCompatActivity {

    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);

        tvResult = (TextView) findViewById(R.id.tv_result);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void getWeather(View view) {

        WeatherApi2 weatherApi2 = RetrofitConfig.getInstance().create(WeatherApi2.class);

        final Gson gson = new GsonBuilder().create();

        weatherApi2.getWeather2()
                .subscribeOn(Schedulers.io())  //在IO线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread()) //回到主线程去处理请求结果
                .subscribe(new Observer<MyResponse<Weather>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(MyResponse<Weather> value) {

                        tvResult.setText(gson.toJson(value));
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
