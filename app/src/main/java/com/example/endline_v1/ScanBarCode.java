package com.example.endline_v1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Calendar;

public class ScanBarCode extends AppCompatActivity {

    EditText ed_barcode, ed_productName, ed_category, ed_price, ed_buyDay, ed_endline;
    DatePickerDialog.OnDateSetListener callback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanpage);

        ed_barcode = (EditText) findViewById(R.id.ed_barcode);
        ed_productName = (EditText) findViewById(R.id.ed_productName);
        ed_price = (EditText) findViewById(R.id.ed_price);
        ed_buyDay = (EditText) findViewById(R.id.ed_buyDay);
        ed_endline = (EditText) findViewById(R.id.ed_endline);

        callback = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                ed_buyDay.setText(year + "." + month + "." + dayOfMonth);
            }
        };

        ed_buyDay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getApplicationContext(), callback, year, month, day);
                datePickerDialog.show();

                Toast.makeText(getApplicationContext(), "onTouch", Toast.LENGTH_SHORT).show();

                return true;
            }
        });

        new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "ISBN : " + result.getContents(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
