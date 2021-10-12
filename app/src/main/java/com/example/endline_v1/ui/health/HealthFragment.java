package com.example.endline_v1.ui.health;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.endline_v1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HealthFragment extends Fragment {

    private HealthViewModel healthViewModel;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    CollectionReference collectionReference = firestore.collection("mainData");
    Query query;
    FirebaseAuth user = FirebaseAuth.getInstance();

    ListView lv_health;
    ArrayAdapter adapter;
    ArrayList listItem;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        healthViewModel =
                new ViewModelProvider(this).get(HealthViewModel.class);
        View root = inflater.inflate(R.layout.fragment_health, container, false);
        final TextView textView = root.findViewById(R.id.text_health);
        healthViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

//        lv_health = (ListView) root.findViewById(R.id.list);
//        listItem = new ArrayList();
//
//        adapter = new ArrayAdapter(getActivity(), R.layout.fragment_health, R.id.tv_healthItem, listItem);
//        lv_health.setAdapter(adapter);

        getData();

        return root;
    }

    private void getData(){
        query = collectionReference.whereEqualTo("카테고리", "건강");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Log.d("getData", document.getData().toString());
//                        listItem.add(document.get("제품명"));
//                        adapter.notifyDataSetChanged();
                    }
                }else{
                    Log.w("getData", "fail");
                }
            }
        });
    }
}