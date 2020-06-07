package com.kazakovdmitri.homework10_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String ATRIBUTE_NAME_ONE = "one";
    private static final String ATRIBUTE_NAME_TWO = "two";
    private static final String PREFERENCES_NAME = "my_pref";
    private static final String PREFERENCES_KEY = "values";

    List<Map<String, String>> simpleAdaptercontent = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ListView list = findViewById(R.id.list);
        prepareContent();
        final BaseAdapter listContentAdapter = createAdapter(simpleAdaptercontent);
        list.setAdapter(listContentAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                simpleAdaptercontent.remove(position);
                listContentAdapter.notifyDataSetChanged();
            }
        });


        final SwipeRefreshLayout swipe = findViewById(R.id.swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                simpleAdaptercontent.clear();
                prepareContent();
                listContentAdapter.notifyDataSetChanged();
                swipe.setRefreshing(false);
            }
        });

    }

    @NonNull
    private BaseAdapter createAdapter(List<Map<String, String>> values) {
        String[] from = {ATRIBUTE_NAME_ONE, ATRIBUTE_NAME_TWO};
        int[] to = {R.id.tv_one, R.id.tv_two};
        return new SimpleAdapter(this, values, R.layout.list_item, from, to);
    }

    @NonNull
    private void prepareContent() {
        try {
            prepareContentFromPrefs();
        } catch (Exception e) {
            e.printStackTrace();
            prepareContentFromAssets();
            SharedPreferences mySharedPref = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
            mySharedPref.edit().putString(PREFERENCES_KEY, getString(R.string.large_text)).apply();
        }
    }


    private void prepareContentFromPrefs() throws Exception{
        SharedPreferences mySharedPref = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        String savedStr = mySharedPref.getString(PREFERENCES_KEY, "");
        String[] strings;

        if (!savedStr.isEmpty()) {
            strings = savedStr.split("\n\n");
        } else {
            throw new Exception("SharedPreferences has no values");
        }

        for (String str : strings) {
            Map <String, String> map = new HashMap<>();
            map.put(ATRIBUTE_NAME_ONE, str.length() + "");
            map.put(ATRIBUTE_NAME_TWO, str);
            simpleAdaptercontent.add(map);
        }
    }

    private void prepareContentFromAssets() {
        String[] strings = getString(R.string.large_text).split("\n\n");
        for (String str : strings) {
            Map<String, String> map = new HashMap<>();
            map.put(ATRIBUTE_NAME_ONE, str.length() + "");
            map.put(ATRIBUTE_NAME_TWO, str);
            simpleAdaptercontent.add(map);
        }
    }
}
