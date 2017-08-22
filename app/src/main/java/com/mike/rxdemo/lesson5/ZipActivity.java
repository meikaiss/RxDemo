package com.mike.rxdemo.lesson5;

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
import io.reactivex.functions.BiFunction;

/**
 * 学习了Zip的基本用法, 那么它在Android有什么用呢, 其实很多场景都可以用到Zip. 举个例子.
 * <p>
 * 比如一个界面需要展示用户的一些信息, 而这些信息分别要从两个服务器接口中获取,
 * 而只有当两个都获取到了之后才能进行展示, 这个时候就可以用Zip了:
 * Created by meikai on 17/8/22.
 */
public class ZipActivity extends AppCompatActivity {

    private TextView tvResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson5);

        tvResult = (TextView) findViewById(R.id.tv_result);
    }

    public void btnStart(View view) {
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                appendResult("1号源头发送事件1");
                emitter.onNext(1);
                appendResult("1号源头发送事件2");
                emitter.onNext(2);
                appendResult("1号源头发送事件3");
                emitter.onNext(3);
                appendResult("1号源头发送事件4");
                emitter.onNext(4);
                appendResult("1号源头发送事件onComplete");
                emitter.onComplete();
            }
        });

        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                appendResult("2号源头发送事件--A");
                e.onNext("A");
                appendResult("2号源头发送事件--B");
                e.onNext("B");
                appendResult("2号源头发送事件--C");
                e.onNext("C");
                appendResult("2号源头发送事件--onComplete");
                e.onComplete();
            }
        });

        Observable zipObservable = Observable.zip(observable1, observable2, new BiFunction<Integer, String, Msg>() {
            @Override
            public Msg apply(Integer integer, String s) throws Exception {
                appendResult("zip合并事件--" + integer + "," + s);
                return new Msg(integer, s);
            }
        });

        Observer<Msg> observer = new Observer<Msg>() {
            @Override
            public void onSubscribe(Disposable d) {
                appendResult("接收到事件onSubscribe");
            }

            @Override
            public void onNext(Msg value) {
                appendResult("接收到事件" + value.toString());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                appendResult("接收到事件onComplete");
            }
        };

        zipObservable.subscribe(observer);
    }

    private void appendResult(String tip) {
        String result = tvResult.getText().toString();

        if (result != null && result.length() > 0) {
            result += "\n";
        }

        result += tip;

        tvResult.setText(result);
    }

    public static final class Msg {
        public int age;
        public String name;

        public Msg(int age, String name) {
            this.age = age;
            this.name = name;
        }

        @Override
        public String toString() {
            return "age=" + age + ",name=" + name;
        }
    }

}
