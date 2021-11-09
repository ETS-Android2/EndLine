package com.example.endline_v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

public class ItemInfo extends AppCompatActivity {

    private Intent intent_info;
    private ActionBar actionBar;
    private FirebaseFirestore firestore;
    private CollectionReference collectionReference;
    private Query query;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String product_name;

    private TextView
            tv_info_product_name, tv_info_category, tv_info_register_date,
            tv_info_barcode, tv_info_brand, tv_info_price, tv_info_buy_date, tv_info_endline;
    private ImageView iv_info_img;
    private Button btn_info_update, btn_info_delete, btn_info_use, btn_info_not_use;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        actionBar = getSupportActionBar();
        actionBar.setTitle("상세 정보");
        actionBar.setDisplayHomeAsUpEnabled(true);

        iv_info_img = (ImageView) findViewById(R.id.iv_info_img);
        tv_info_product_name = (TextView) findViewById(R.id.tv_info_product_name);
        tv_info_category = (TextView) findViewById(R.id.tv_info_category);
        tv_info_register_date = (TextView) findViewById(R.id.tv_info_register_Day);
        tv_info_barcode = (TextView) findViewById(R.id.tv_info_barcode);
        tv_info_brand = (TextView)findViewById(R.id.tv_info_brand);
        tv_info_price = (TextView) findViewById(R.id.tv_info_price);
        tv_info_buy_date = (TextView) findViewById(R.id.tv_info_buyDay);
        tv_info_endline = (TextView) findViewById(R.id.tv_info_endline);
        btn_info_update = (Button) findViewById(R.id.btn_info_update);
        btn_info_delete = (Button) findViewById(R.id.btn_info_delete);
        btn_info_use = (Button) findViewById(R.id.btn_info_use);
        btn_info_not_use = (Button)findViewById(R.id.btn_info_not_use);

        intent_info = getIntent();
        product_name = intent_info.getStringExtra("product_name");

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        collectionReference = firestore.collection("mainData");

        getData(product_name);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_info_delete:
                    delete();
            }
        }
    };

    private void delete(){

    }

    private void getData(String product_name) {
        query = collectionReference.whereEqualTo("UID", user.getUid()).whereEqualTo("product_name", product_name);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
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

        tv_info_product_name.setText(document.get("product_name").toString());
        tv_info_category.setText(document.get("category").toString());
        tv_info_register_date.setText(document.get("register_date").toString());
        tv_info_barcode.setText(document.get("barcode").toString());
        tv_info_brand.setText(document.get("brand").toString());
        tv_info_price.setText(document.get("price").toString());
        tv_info_buy_date.setText(document.get("buy_date").toString());
        tv_info_endline.setText(document.get("end_line").toString());
    }
}