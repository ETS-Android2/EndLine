package com.example.endline_v1.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.endline_v1.R;

public class EnterNumber extends AppCompatActivity {

    Button btn_enter_number;
    EditText et_barcodeNum;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_number);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("번호 입력");
        actionBar.setDisplayHomeAsUpEnabled(true);

        et_barcodeNum = (EditText) findViewById(R.id.et_barcodeNum);

        btn_enter_number = (Button) findViewById(R.id.btn_enter_number);
        btn_enter_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DirectlyAdd.class);
                intent.putExtra("barcode_number", et_barcodeNum.getText().toString());
                startActivity(intent);
                finish();
            }
        });
    }
}