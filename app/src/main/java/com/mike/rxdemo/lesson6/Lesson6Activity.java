package com.mike.rxdemo.lesson6;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.mike.rxdemo.R;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;

/**
 * Created by meikai on 17/8/23.
 */
public class Lesson6Activity extends AppCompatActivity {

    private TextView tvResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson6);

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


    public void btnStart(View view) {

        //创建一个可以被别人订阅的对象，事件通过Integer类型的对象进行传递
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {

                for (int i = 1; i < 50; i++) {
                    appendResult("发送事件" + i);
                    emitter.onNext(i);
                }

                appendResult("发送事件onComplete");
                emitter.onComplete();
            }
        });

        //创建一个订阅对象
        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

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
        observable.filter(new Predicate<Integer>() {

            /**
             * @param integer
             * @return true:是有效的事件，可以发送到下游; false:是无效事件，阻止发送到下游
             * @throws Exception
             */
            @Override
            public boolean test(Integer integer) throws Exception {
                if (integer % 5 == 0) {
                    appendResult("-有效事件，可以下发" + integer);
                    return true;
                }
                appendResult("-无效事件，阻止下发" + integer);
                return false;
            }
        }).subscribe(observer);

    }
}
