package com.example.endline_v1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

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
        et_barcodeNum.setFilters(new InputFilter[]{editFilter});

        btn_enter_number = (Button) findViewById(R.id.btn_enter_number);
        btn_enter_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ScanBarCode.class);
                intent.putExtra("number", et_barcodeNum.getText());
                startActivity(intent);
                finish();
            }
        });
    }

    protected InputFilter editFilter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern pattern = Pattern.compile("^[0-9]*$");        // 숫자
            // Pattern pattern = Pattern.compile("^[a-zA-Z]+$");        // 영문
            // Pattern pattern = Pattern.compile("^[ㄱ-ㅎ가-힣]+$");        // 한글
            // Pattern pattern = Pattern.compile("^[a-zA-Z0-9ㄱ-ㅎ가-힣]+$");        // 영문,숫자,한글
            if(!pattern.matcher(source).matches()) {
                Toast.makeText(getApplicationContext(), "숫자를 입력해주세요", Toast.LENGTH_SHORT).show();
                return "";
            }
            return null;
        }
    };
}