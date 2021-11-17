package com.example.endline_v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {

    private ArrayList<DataSet> list;
    private SettingRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private Button btn_add, btn_clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("알림");
        actionBar.setDisplayHomeAsUpEnabled(true);

//        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        list = new ArrayList<>();
//        adapter = new SettingRecyclerAdapter(list);
//        recyclerView.setAdapter(adapter);
//
//        btn_add = (Button) findViewById(R.id.btn_itemadd);
//        btn_clear = (Button) findViewById(R.id.btn_itemdel);
//
//        btn_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DataSet data = new DataSet("유통기한 임박 확인!!!");
//                list.add(data);
//                adapter.notifyDataSetChanged();
//            }
//        });
//
//        btn_clear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                list.clear();
//                adapter.notifyDataSetChanged();
//            }
//        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}