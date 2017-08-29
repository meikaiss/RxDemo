package com.mike.rxdemo.lesson8;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mike.rxdemo.R;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.disposables.Disposable;

/**
 * Created by meikai on 17/8/23.
 */
public class Lesson8Activity extends AppCompatActivity {

    private static final String TAG = "Lesson8Activity";

    private TextView tvResult;

    private Disposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson8);

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
        if (disposable != null) {
            disposable.dispose();
        }
    }

    public void btnStart(View view) {

        Flowable<Integer> flowable = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                appendResult("emit 1");
                emitter.onNext(1);
                appendResult("emit 2");
                emitter.onNext(2);
                appendResult("emit 3");
                emitter.onNext(3);
                appendResult("onComplete");
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR);

        //这个参数是用来选择背压,也就是出现上下游流速不均衡的时候应该怎么处理的办法, 这里我们直接用BackpressureStrategy.ERROR这种方式, 这种方式会在出现上下游流速不均衡的时候直接抛出一个异常,
        // 这个异常就是著名的MissingBackpressureException

        Subscriber<Integer> subscriber = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                appendResult("onSubscribe");

//                s.request(Long.MAX_VALUE);  //注意这句代码

                //因为下游没有调用request, 上游就认为下游没有处理事件的能力, 而这又是一个同步的订阅, 既然下游处理不了, 那上游不可能一直等待吧, 如果是这样, 万一这两根水管工作在主线程里,
                // 界面不就卡死了吗, 因此只能抛个异常来提醒我们. 那如何解决这种情况呢, 很简单啦, 下游直接调用request(Long.MAX_VALUE)就行了,
                // 或者根据上游发送事件的数量来request就行了, 比如这里request(3)就可以了.
            }

            @Override
            public void onNext(Integer integer) {
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
