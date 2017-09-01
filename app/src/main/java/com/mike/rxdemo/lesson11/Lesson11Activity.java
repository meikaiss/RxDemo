package com.mike.rxdemo.lesson11;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.mike.rxdemo.R;

import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by meikai on 17/8/23.
 */
public class Lesson11Activity extends AppCompatActivity {

    private static final String TAG = "Lesson11Activity";

    private EditText editText;
    private TextView tvResult;

    private PublishSubject<String> publishSubject;
    private Subscription subscription;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson11);

        editText = (EditText) findViewById(R.id.edit_text);
        tvResult = (TextView) findViewById(R.id.tv_result);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                publishSubject.onNext(s.toString());
            }
        });

        publishSubject = PublishSubject.create();
        init();
    }

    private void init() {

        //用debounce操作符，当输入框发生变化时，不会立刻将事件发送给下游，而是等待500ms，
        // 如果在这段事件内，输入框没有发生变化，那么才发送该事件；反之，则在收到新的关键词后，继续等待200ms。
        Observable<String> observable = publishSubject.debounce(500, TimeUnit.MILLISECONDS)
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        //只有关键词的长度大于0时才发送事件给下游。
                        return s != null && s.length() > 0;
                    }
                }).switchMap(new Function<String, ObservableSource<? extends String>>() {
                    @Override
                    public ObservableSource<? extends String> apply(String s) throws Exception {
                        //当发起了abc的请求之后，即使ab的结果返回了，也不会发送给下游，从而避免了出现前面介绍的搜索词和联想结果不匹配的问题。
                        return getSearchObservable(s);
                    }
                }).observeOn(AndroidSchedulers.mainThread());

        DisposableObserver<String> observer = new DisposableObserver<String>() {
            @Override
            public void onNext(String value) {
                appendResult("onNext：" + value);
            }

            @Override
            public void onError(Throwable e) {
                appendResult("onError：" + e.getMessage());
            }

            @Override
            public void onComplete() {
                appendResult("onComplete：");
            }
        };

        observable.subscribe(observer);
    }

    private Observable<String> getSearchObservable(final String query) {
        return Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> observableEmitter) throws Exception {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        appendResult("开始请求，关键词为：" + query + ",2000-2500ms后返回");
                    }
                });
                try {
                    Thread.sleep(2000 + (long) (Math.random() * 500));
                } catch (InterruptedException e) {
                    if (!observableEmitter.isDisposed()) {
                        observableEmitter.onError(e);
                    }
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        appendResult("结束请求，关键词为：" + query);
                    }
                });

                observableEmitter.onNext("完成搜索，关键词为：" + query);
                observableEmitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());
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

}
