package com.example.endline_v1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DirectlyAdd extends AppCompatActivity {

    private EditText et_barcode, et_product_name, et_category, et_price, et_buyDay, et_endline, et_brand;
    private Button btn_buyDatePicker, btn_endLinePicker, btn_insertScan, btn_cancelScan;
    private ImageButton ibtn_selectPhoto;
    private Spinner spinner;
    private Map<String, Object> data = new HashMap<>();
    private Calendar c = Calendar.getInstance();
    private FirebaseUser user;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private CollectionReference collectionReference;
    private Query query;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private Uri imgUri;
    private String imgUrl;
    private String randomKey;
    private Intent intent;
    private String barcode_number;

    static final int REQ_SELECT_PHOTO = 111;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanpage);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("등록");
        actionBar.setDisplayHomeAsUpEnabled(true);

        et_barcode = (EditText) findViewById(R.id.et_barcode);
        et_product_name = (EditText) findViewById(R.id.et_product_name);
        et_price = (EditText) findViewById(R.id.et_price);
        et_category = (EditText) findViewById(R.id.et_category);
        et_buyDay = (EditText) findViewById(R.id.et_buyDay);
        et_endline = (EditText) findViewById(R.id.et_endline);
        et_brand = (EditText) findViewById(R.id.et_brand);
        btn_buyDatePicker = (Button) findViewById(R.id.btn_buyDatePicker);
        btn_endLinePicker = (Button) findViewById(R.id.btn_endLinePicker);
        btn_insertScan = (Button) findViewById(R.id.btn_insertScan);
        btn_cancelScan = (Button) findViewById(R.id.btn_cancelScan);
        ibtn_selectPhoto = (ImageButton) findViewById(R.id.ibtn_selectPhoto);
        spinner = (Spinner) findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                et_category.setText(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        DatePickerDialog.OnDateSetListener buyDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                et_buyDay.setText(year + "-" + (month+1) + "-" + dayOfMonth);
            }
        };

        DatePickerDialog.OnDateSetListener endLineDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                et_endline.setText(year + "-" + (month+1) + "-" + dayOfMonth);
            }
        };

        View.OnClickListener showDatePicker = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.btn_buyDatePicker){
                    new DatePickerDialog(
                            DirectlyAdd.this,
                            buyDateSetListener,
                            c.get(Calendar.YEAR),
                            c.get(Calendar.MONTH),
                            c.get(Calendar.DAY_OF_MONTH)).show();
                }else if(v.getId() == R.id.btn_endLinePicker){
                    new DatePickerDialog(
                            DirectlyAdd.this,
                            endLineDateSetListener,
                            c.get(Calendar.YEAR),
                            c.get(Calendar.MONTH),
                            c.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        };

        btn_buyDatePicker.setOnClickListener(showDatePicker);
        btn_endLinePicker.setOnClickListener(showDatePicker);
        ibtn_selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQ_SELECT_PHOTO);
            }
        });
        
        btn_insertScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validate = ValidateData(
                        et_barcode.getText().toString(),
                        et_brand.getText().toString(),
                        et_buyDay.getText().toString(),
                        et_endline.getText().toString(),
                        et_category.getText().toString(),
                        et_product_name.getText().toString(),
                        et_price.getText().toString()
                );
                if(validate){
                    if (imgUri != null || imgUrl != null) {
                        if(imgUrl != null){
                            InsertDataWithUrl(imgUrl);
                        }else{
                            InsertDataWithUri(imgUri);
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "이미지를 선택해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        
        btn_cancelScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_SHORT).show();
            }
        });

        intent = getIntent();
        if(intent.hasExtra("barcode_number")){
            barcode_number = intent.getStringExtra("barcode_number");
            et_barcode.setText(barcode_number);
            getDataFormFirebase(barcode_number);
        }
    }

    private boolean ValidateData(
            String barcode, String brand, String buyDay, String endline, String category, String product_name, String price) {
        if(barcode.isEmpty()) {
            Toast.makeText(this, "물품 정보를 입력하세요!", Toast.LENGTH_SHORT).show();
            return false;
        }else if(brand.isEmpty()){
            Toast.makeText(this, "물품 정보를 입력하세요!", Toast.LENGTH_SHORT).show();
            return false;
        }else if(buyDay.isEmpty()){
            Toast.makeText(this, "물품 정보를 입력하세요!", Toast.LENGTH_SHORT).show();
            return false;
        }else if(endline.isEmpty()){
            Toast.makeText(this, "물품 정보를 입력하세요!", Toast.LENGTH_SHORT).show();
            return false;
        }else if(category.isEmpty()){
            Toast.makeText(this, "물품 정보를 입력하세요!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(category.equals("카테고리")) {
            Toast.makeText(this, "카테고리를 선택하세요!", Toast.LENGTH_SHORT).show();
            return false;
        }else if(product_name.isEmpty()){
            Toast.makeText(this, "물품 정보를 입력하세요!", Toast.LENGTH_SHORT).show();
            return false;
        }else if(price.isEmpty()){
            Toast.makeText(this, "물품 정보를 입력하세요!", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }

    private void InsertDataWithUrl(String imgurl) {
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        data.put("UID", user.getUid());
        data.put("barcode", et_barcode.getText().toString());
        data.put("register_date", getTime());
        data.put("product_name", et_product_name.getText().toString());
        data.put("brand", et_brand.getText().toString());
        data.put("img", imgurl);
        data.put("category", et_category.getText().toString());
        data.put("price", et_price.getText().toString());
        data.put("buy_date", et_buyDay.getText().toString());
        data.put("end_line", et_endline.getText().toString());
        data.put("use_state", "false");
        firestore.collection("mainData").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("Firestore 입력", "write data to firebase");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("firestore input", "write data fail");
            }
        });
        finish();
        Toast.makeText(getApplicationContext(), "입력 완료", Toast.LENGTH_SHORT).show();

        data.clear();
    }

    private void InsertDataWithUri(Uri imgUri) {
        randomKey = UUID.randomUUID().toString();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference("images/" + randomKey);

        storageRef.putFile(imgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    storageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                firestore = FirebaseFirestore.getInstance();
                                auth = FirebaseAuth.getInstance();
                                user = auth.getCurrentUser();

                                data.put("UID", user.getUid());
                                data.put("barcode", et_barcode.getText().toString());
                                data.put("register_date", getTime());
                                data.put("product_name", et_product_name.getText().toString());
                                data.put("brand", et_brand.getText().toString());
                                data.put("img", task.getResult().toString());
                                data.put("category", et_category.getText().toString());
                                data.put("price", et_price.getText().toString());
                                data.put("buy_date", et_buyDay.getText().toString());
                                data.put("end_line", et_endline.getText().toString());
                                data.put("use_state", "false");
                                firestore.collection("mainData").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d("Firestore 입력", "write data to firebase");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("firestore input", "write data fail");
                                    }
                                });
                                finish();
                                Toast.makeText(getApplicationContext(), "입력 완료", Toast.LENGTH_SHORT).show();

                                data.clear();
                            }else{
                                Log.w("put Img", "fail");
                            }
                        }
                    });
                }else{
                    Log.w("put Img", "fail");
                }
            }
        });
    }

    private void getDataFormFirebase(String barcode_number) {
        firestore = FirebaseFirestore.getInstance();
        collectionReference = firestore.collection("mainData");
        query = collectionReference.whereEqualTo("barcode", barcode_number);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Uri uri = Uri.parse(document.get("img").toString());
                        Glide.with(getApplicationContext()).load(uri).into(ibtn_selectPhoto);
                        imgUrl = document.get("img").toString();
                        et_brand.setText(document.get("brand").toString());
                        et_category.setText(document.get("category").toString());
                        et_category.setVisibility(View.VISIBLE);
                        spinner.setVisibility(View.GONE);
                        et_product_name.setText(document.get("product_name").toString());
                        et_price.setText(document.get("price").toString());
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "등록된 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getTime(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = dateFormat.format(date);
        return getTime;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQ_SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    try{
                        InputStream inputStream = getContentResolver().openInputStream(data.getData());
                        Bitmap img = BitmapFactory.decodeStream(inputStream);
                        inputStream.close();

                        ibtn_selectPhoto.setImageBitmap(img);
                        imgUri = data.getData();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
