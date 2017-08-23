package com.mike.rxdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mike.rxdemo.lesson1.Lesson1Activity;
import com.mike.rxdemo.lesson2.Lesson2Activity;
import com.mike.rxdemo.lesson3.Lesson3Activity;
import com.mike.rxdemo.lesson4.Lesson4Activity;
import com.mike.rxdemo.lesson5.ZipActivity;
import com.mike.rxdemo.lesson6.Lesson6Activity;
import com.mike.rxdemo.lesson7.Lesson7Activity;


/**
 * Created by meikai on 17/8/22.
 */
public class MainActivity extends AppCompatActivity {

    private ListView listView;

    private Class<?>[] activityClassList = {
            Lesson1Activity.class,
            Lesson2Activity.class,
            Lesson3Activity.class,
            Lesson4Activity.class,
            ZipActivity.class,
            Lesson6Activity.class,
            Lesson7Activity.class
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list_view);

        listView.setAdapter(new ArrayAdapter<Class<?>>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, activityClassList));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, activityClassList[position]);

                MainActivity.this.startActivity(intent);
            }
        });

    }
}
