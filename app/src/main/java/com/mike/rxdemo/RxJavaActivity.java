package com.mike.rxdemo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by meikai on 17/8/21.
 */
public class RxJavaActivity extends AppCompatActivity {

    private TextView tvResult;
    private TextView tvResultAppCount;

    /**
     * 方式一：
     */
    private Disposable disposable;

    /**
     * 方式二：
     * RxJava中已经内置了一个容器CompositeDisposable,
     * 每当我们得到一个Disposable时就调用CompositeDisposable.add()将它添加到容器中,
     * 在退出的时候, 调用CompositeDisposable.clear() 即可切断所有的水管.
     */
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);

        tvResult = (TextView) findViewById(R.id.tv_result);
        tvResultAppCount = (TextView) findViewById(R.id.tv_result_app_count);
    }

    @Override
    protected void onDestroy() {
        if (disposable != null) {
            disposable.dispose();
        }
        compositeDisposable.clear();
        super.onDestroy();
    }

    public void getAppCount(View view) {
        tvResultAppCount.setText("查询中");

        Observable.create(new ObservableOnSubscribe<PackageInfo>() {
            @Override
            public void subscribe(ObservableEmitter<PackageInfo> e) throws Exception {
                PackageManager pm = RxJavaActivity.this.getPackageManager();
                // 查询所有已经安装的应用程序
                List<PackageInfo> appInfos = pm.getInstalledPackages(0);
                // GET_UNINSTALLED_PACKAGES代表已删除，但还有安装目录的

                for (int i = 0; i < appInfos.size(); i++) {
                    Thread.sleep(20);
                    e.onNext(appInfos.get(i));
                }

                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<PackageInfo, String>() {
                    @Override
                    public String apply(PackageInfo packageInfo) throws Exception {
                        PackageManager pManager = RxJavaActivity.this.getPackageManager();
                        return pManager.getApplicationLabel(packageInfo.applicationInfo).toString();
                    }
                })
                .subscribe(new Observer<String>() {

                    int count;

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String appName) {
                        count++;

                        tvResultAppCount.setText("共安装了" + count + "个应用,名称＝" + appName);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
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
                        //断水管, 使得下游收不到事件, 既然收不到事件, 那么也就不会再去更新UI了.
                        // 因此我们可以在Activity中将这个Disposable 保存起来, 当Activity退出时, 切断它即可.
                        disposable = d;
                        compositeDisposable.add(d);
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
