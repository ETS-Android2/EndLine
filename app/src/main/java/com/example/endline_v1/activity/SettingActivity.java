package com.example.endline_v1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import com.example.endline_v1.R;
import com.example.endline_v1.dto.DataSet;
import com.example.endline_v1.util.SettingRecyclerAdapter;

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

        //  display notice from server

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}