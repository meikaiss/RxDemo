package com.mike.rxdemo.lesson10;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mike.rxdemo.R;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by meikai on 17/8/23.
 */
public class Lesson10Activity extends AppCompatActivity {

    private static final String TAG = "Lesson8Activity";

    private TextView tvResult;

    private Subscription subscription;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson10);

        tvResult = (TextView) findViewById(R.id.tv_result);

    }

    private void appendResult(String tip) {
        Log.d(TAG, tip);

        String result = tvResult.getText().toString();

        if (result != null && result.length() > 0) {
            result += "\n";
        }

        result += tip;

        tvResult.setText(result);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null) {
            subscription.cancel();
        }
    }


    public void btnGetOne(View view) {
        subscription.request(1);
    }

    public void btnStart(View view) {

        Flowable<Long> flowable = Flowable.interval(1, TimeUnit.MICROSECONDS)
                .onBackpressureLatest()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        //这个参数是用来选择背压,也就是出现上下游流速不均衡的时候应该怎么处理的办法, 这里我们直接用BackpressureStrategy.ERROR这种方式, 这种方式会在出现上下游流速不均衡的时候直接抛出一个异常,
        // 这个异常就是著名的MissingBackpressureException

        Subscriber<Long> subscriber = new Subscriber<Long>() {
            @Override
            public void onSubscribe(Subscription s) {
                appendResult("onSubscribe");

                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Long integer) {
                appendResult("onNext: " + integer);
            }

            @Override
            public void onError(Throwable t) {
                appendResult("onError: " + t.toString());
            }

            @Override
            public void onComplete() {
                appendResult("onCromplete");
            }
        };

        flowable.subscribe(subscriber);

    }
}
