package com.example.endline_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class LoginFormActivity extends AppCompatActivity {

    private TextView tv_id;
    private ImageView iv_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);

        Intent intent = getIntent();
        String id = intent.getStringExtra("nickName");
        String photoUrl = intent.getStringExtra("photoUrl");

        tv_id = (TextView) findViewById(R.id.tv_id);
        iv_profile = (ImageView) findViewById(R.id.iv_profile);

        tv_id.setText(id);
        Glide.with(this).load(photoUrl).into(iv_profile);

    }
}