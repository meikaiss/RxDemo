package com.mike.rxdemo.lesson2;

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

/**
 * Created by meikai on 17/8/22.
 */
public class Lesson2Activity extends AppCompatActivity {


    private TextView tvResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson2);

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
                appendResult("发送事件1");
                emitter.onNext(1);
                appendResult("发送事件2");
                emitter.onNext(2);
                appendResult("发送事件3");
                emitter.onNext(3);
                appendResult("发送事件onComplete");
                emitter.onComplete();
                appendResult("发送事件4");
                emitter.onNext(4);
            }
        });

        //创建一个订阅对象
        Observer<Integer> observer = new Observer<Integer>() {

            private Disposable disposable;

            @Override
            public void onSubscribe(Disposable d) {
                appendResult("--处理事件onSubscribe");
                disposable = d;
            }

            @Override
            public void onNext(Integer value) {
                if (value == 2) {
                    appendResult("--切断水管" + value);
                    disposable.dispose();
                }
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
