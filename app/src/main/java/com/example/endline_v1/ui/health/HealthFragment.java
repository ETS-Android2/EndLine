package com.example.endline_v1.ui.health;

import android.app.Activity;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.endline_v1.DataSet;
import com.example.endline_v1.ItemDataSet;
import com.example.endline_v1.ItemRecyclerAdapter;
import com.example.endline_v1.MainActivity;
import com.example.endline_v1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class HealthFragment extends Fragment {

    private HealthViewModel healthViewModel;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    CollectionReference collectionReference = firestore.collection("mainData");
    Query query;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    private ArrayList<ItemDataSet> list;
    private RecyclerView recyclerView;
    private ItemRecyclerAdapter adapter;
    private Activity mainActivity;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.mainActivity = activity;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        healthViewModel =
                new ViewModelProvider(this).get(HealthViewModel.class);
        View root = inflater.inflate(R.layout.fragment_health, container, false);
//        final TextView textView = root.findViewById(R.id.text_health);
//        healthViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        list = new ArrayList<>();
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView_health);
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity.getApplicationContext()));
        adapter = new ItemRecyclerAdapter(list);
        recyclerView.setAdapter(adapter);

        getData();

        return root;
    }

    private void getData(){
        Log.d("UID", user.getUid());
        query = collectionReference.whereEqualTo("카테고리", "건강").whereEqualTo("UID", user.getUid());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        if(document.exists()){
                            Log.d("getData", document.getData().toString());
                            ItemDataSet itemDataSet = new ItemDataSet(
                                    document.get("제품명").toString(),
                                    document.get("카테고리").toString(),
                                    document.get("구매 일자").toString(),
                                    document.get("유통 기한").toString(),
                                    document.get("이미지").toString()
                            );
                            list.add(itemDataSet);
                            adapter.notifyDataSetChanged();
                        }else{
                            Log.w("getData", "No Data in uid");
                        }

                    }
                }else{
                    Log.w("getData", "fail");
                }
            }
        });
    }
}