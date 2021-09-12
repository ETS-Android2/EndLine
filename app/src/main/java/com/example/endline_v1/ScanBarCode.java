package com.example.endline_v1;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Calendar;

public class ScanBarCode extends AppCompatActivity {

    EditText ed_barcode, ed_productName, ed_category, ed_price, ed_buyDay, ed_endline;
    Button btn_buyDatePicker, btn_endLinePicker, btn_insertScan, btn_cancelScan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanpage);

        ed_barcode = (EditText) findViewById(R.id.ed_barcode);
        ed_productName = (EditText) findViewById(R.id.ed_productName);
        ed_price = (EditText) findViewById(R.id.ed_price);
        ed_buyDay = (EditText) findViewById(R.id.ed_buyDay);
        ed_endline = (EditText) findViewById(R.id.ed_endline);
        btn_buyDatePicker = (Button) findViewById(R.id.btn_buyDatePicker);
        btn_endLinePicker = (Button) findViewById(R.id.btn_endLinePicker);
        btn_insertScan = (Button) findViewById(R.id.btn_insertScan);
        btn_cancelScan = (Button) findViewById(R.id.btn_cancelScan);

        View.OnClickListener showDatePicker = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ScanBarCode.this).show();
            }
        };

        btn_buyDatePicker.setOnClickListener(showDatePicker);
        btn_endLinePicker.setOnClickListener(showDatePicker);
        
        btn_insertScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Toast.makeText(getApplicationContext(), "입력 버튼", Toast.LENGTH_SHORT).show();
            }
        });
        
        btn_cancelScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Toast.makeText(getApplicationContext(), "취소 버튼", Toast.LENGTH_SHORT).show();
            }
        });

        new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        ed_barcode.setText("바코드 번호 : " + result.getContents());
//        Toast.makeText(this, "ISBN : " + result.getContents(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
