package com.mike.rxdemo.lesson7;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.mike.rxdemo.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by meikai on 17/8/23.
 */
public class Lesson7Activity extends AppCompatActivity {

    private TextView tvResult;

    private Disposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson7);

        tvResult = (TextView) findViewById(R.id.tv_result);

    }

    private void appendResult(String tip) {
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
        if (disposable != null) {
            disposable.dispose();
        }
    }

    public void btnStart(View view) {

        //创建一个可以被别人订阅的对象，事件通过Integer类型的对象进行传递
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {

                for (int i = 1; i < 50; i++) {
                    final int finalI = i;
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            appendResult("发送事件" + finalI);
                        }
                    });

                    emitter.onNext(i);

                    Thread.sleep(200);// 因为sample设置是每2秒才取一个事件 下发，如果这里一次性发完了，2秒后就只有onComplete事件了
                }

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        appendResult("发送事件onComplete");
                    }
                });

                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .sample(2, TimeUnit.SECONDS)  // 它每隔2秒取一个事件给下游
                .observeOn(AndroidSchedulers.mainThread());

        //创建一个订阅对象
        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(Integer value) {
                appendResult("--处理事件" + value);
            }

            @Override
            public void onError(Throwable e) {
                appendResult("--处理事件onError," + e.getMessage());
            }

            @Override
            public void onComplete() {
                appendResult("处理事件onComplete");
            }

        };

        //创建连接，将 被观察者的信息 传递给 观察者
        observable.subscribe(observer);

    }
}
