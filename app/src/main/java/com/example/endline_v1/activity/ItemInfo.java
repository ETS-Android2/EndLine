package com.example.endline_v1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.example.endline_v1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ItemInfo extends AppCompatActivity {

    private Intent intent_info;
    private ActionBar actionBar;
    private FirebaseFirestore firestore;
    private CollectionReference collectionReference;
    private Query query;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String product_name;
    private String docId;

    private TextView
            tv_info_category, tv_info_register_date,
            tv_info_barcode, tv_info_brand;
    private EditText et_info_product_name, et_info_price, et_info_buy_date, et_info_endline;
    private ImageView iv_info_img;
    private Button btn_info_update, btn_info_delete;
    private ToggleButton toggleButton;
    private boolean updateState = false;

    private static final int REQ_UPDATE = 177;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        actionBar = getSupportActionBar();
        actionBar.setTitle("상세 정보");
        actionBar.setDisplayHomeAsUpEnabled(true);

        iv_info_img = (ImageView) findViewById(R.id.iv_info_img);
        et_info_product_name = (EditText) findViewById(R.id.et_info_product_name);
        tv_info_category = (TextView) findViewById(R.id.tv_info_category);
        tv_info_register_date = (TextView) findViewById(R.id.tv_info_register_Day);
        tv_info_barcode = (TextView) findViewById(R.id.tv_info_barcode);
        tv_info_brand = (TextView)findViewById(R.id.tv_info_brand);
        et_info_price = (EditText) findViewById(R.id.et_info_price);
        et_info_buy_date = (EditText) findViewById(R.id.et_info_buyDay);
        et_info_endline = (EditText) findViewById(R.id.et_info_endline);
        btn_info_update = (Button) findViewById(R.id.btn_info_update);
        btn_info_delete = (Button) findViewById(R.id.btn_info_delete);
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);

        intent_info = getIntent();
        product_name = intent_info.getStringExtra("product_name");

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        collectionReference = firestore.collection("mainData");

        getData(product_name);

        btn_info_delete.setOnClickListener(this.onClickListener);
        btn_info_update.setOnClickListener(this.onClickListener);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateUseState(isChecked, docId);
            }
        });
    }

    private void updateUseState(boolean isChecked, String docId) {
        if(docId != null){
            firestore.document("mainData/" + docId).update("use_state", isChecked + "").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), toggleButton.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_info_delete:
                    delete(docId);
                    break;
                case R.id.btn_info_update:
                    updateState = !updateState;
                    if(updateState){
                        btn_info_update.setText("저장");
                    }else{
                        update(docId);
                        btn_info_update.setText("수정");
                    }
                    ChangeMode();
                    break;
            }
        }
    };

    private void update(String docId) {
        Map<String, Object> data = new HashMap<>();
        data.put("product_name", et_info_product_name.getText().toString());
        data.put("buy_date", et_info_buy_date.getText().toString());
        data.put("end_line", et_info_endline.getText().toString());
        data.put("price", et_info_price.getText().toString());
        firestore.document("mainData/" + docId).update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "수정되었습니다!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "수정실패! 다음에 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void ChangeMode() {
        if(updateState){
            et_info_product_name.setEnabled(true);
            et_info_buy_date.setEnabled(true);
            et_info_endline.setEnabled(true);
            et_info_price.setEnabled(true);
        }else{
            et_info_product_name.setEnabled(false);
            et_info_buy_date.setEnabled(false);
            et_info_endline.setEnabled(false);
            et_info_price.setEnabled(false);
        }
    }

    private void delete(String docId){
        firestore.document("mainData/" + docId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "삭제완료!", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "삭제실패, 다음에 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getData(String product_name) {
        query = collectionReference
                .whereEqualTo("UID", user.getUid())
                .whereEqualTo("product_name", product_name);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        docId = document.getId();
                        Log.d("DocID", docId);
                        displayData(document);
                        Log.d("DOCUMENT", document.toString());
                    }
                }else{
                    //get data fail
                }
            }
        });
    }

    private void displayData(QueryDocumentSnapshot document){
        Uri uri = Uri.parse(document.get("img").toString());
        Glide.with(this).load(uri).into(iv_info_img);

        if(document.get("use_state").equals("true")){
            toggleButton.setChecked(true);
        }else{
            toggleButton.setChecked(false);
        }

        et_info_product_name.setText(document.get("product_name").toString());
        tv_info_category.setText(document.get("category").toString());
        tv_info_register_date.setText(document.get("register_date").toString());
        tv_info_barcode.setText(document.get("barcode").toString());
        tv_info_brand.setText(document.get("brand").toString());
        et_info_price.setText(document.get("price").toString());
        et_info_buy_date.setText(document.get("buy_date").toString());
        et_info_endline.setText(document.get("end_line").toString());
    }
}