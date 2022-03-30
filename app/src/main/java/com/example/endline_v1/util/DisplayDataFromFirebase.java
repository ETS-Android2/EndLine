package com.example.endline_v1.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.endline_v1.dto.ItemDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DisplayDataFromFirebase {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore firestore;
    private CollectionReference collectionReference;
    private Query query;
    private Context context;
    private ArrayList<ItemDataSet> list;
    private RecyclerView recyclerView;
    private ItemRecyclerAdapter adapter;
    private String category, product_name, filter_index, uid;

    public DisplayDataFromFirebase(String category,RecyclerView recyclerView, Context context) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.category = category;
    }

    public DisplayDataFromFirebase(String category, RecyclerView recyclerView, Context context, String product_name){
        this.recyclerView = recyclerView;
        this.context = context;
        this.category = category;
        this.product_name = product_name;
    }

    public DisplayDataFromFirebase(String category, String filter_index, RecyclerView recyclerView, Context context){
        this.recyclerView = recyclerView;
        this.context = context;
        this.category = category;
        switch (filter_index){
            case "등록일자순":
                this.filter_index = "register_date";
                break;
            case "구매일자순":
                this.filter_index = "buy_date";
                break;
            case "유통기한순":
                this.filter_index = "end_line";
                break;
            case "사용여부순":
                this.filter_index = "use_state";
                break;
            case "가격순":
                this.filter_index = "price";
                break;
            default:
                this.filter_index = null;
                break;
        }
    }

    public void DisplayData(){
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        uid = user.getUid();
        firestore = FirebaseFirestore.getInstance();
        collectionReference = firestore.collection("mainData");

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        list = new ArrayList<>();
        adapter = new ItemRecyclerAdapter(list);
        recyclerView.setAdapter(adapter);
        getData();
    }

    private void getData() {
        Log.d("UID", user.getUid());
        if(category == "All"){
            if(filter_index == null){
                query = collectionReference.whereEqualTo("UID", uid);
            }else{
                query = collectionReference.whereEqualTo("UID", uid).orderBy(filter_index, Query.Direction.ASCENDING);
            }
        }else if(category == "search"){
            query = collectionReference.whereEqualTo("UID", uid).whereEqualTo("product_name", product_name);
        }
        else{
            if(filter_index == null){
                query = collectionReference.whereEqualTo("UID", uid).whereEqualTo("category", category);
            }else{
                query = collectionReference
                            .whereEqualTo("UID", uid)
                            .whereEqualTo("category", category)
                            .orderBy(filter_index, Query.Direction.ASCENDING);
            }
        }
        list.clear();
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        if(document.exists()){
                            Log.d("getData", document.getData().toString());
                            ItemDataSet itemDataSet = new ItemDataSet(
                                    document.get("product_name").toString(),
                                    document.get("category").toString(),
                                    document.get("buy_date").toString(),
                                    document.get("end_line").toString(),
                                    document.get("img").toString()
                            );
                            list.add(itemDataSet);
                            adapter.notifyDataSetChanged();
                        }else{
                            Log.w("getData", "No Data in uid");
                            Toast.makeText(context, "아직 데이터가 없습니다!", Toast.LENGTH_LONG).show();
                        }
                    }
                }else{
                    Log.w("getData", task.getResult().toString());
                    Toast.makeText(context, "데이터 로딩 실패\n다시 시도해 보세요", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
