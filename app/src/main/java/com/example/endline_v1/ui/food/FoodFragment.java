package com.example.endline_v1.ui.food;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.endline_v1.DisplayDataFromFirebase;
import com.example.endline_v1.R;

public class FoodFragment extends Fragment {

    private FoodViewModel foodViewModel;
    private RecyclerView recyclerView;
    private Activity activity;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        foodViewModel =
                new ViewModelProvider(this).get(FoodViewModel.class);
        View root = inflater.inflate(R.layout.fragment_food, container, false);
//        final TextView textView = root.findViewById(R.id.text_food);
//        foodViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView_food);
        DisplayDataFromFirebase displayer = new DisplayDataFromFirebase("식품", recyclerView, activity.getApplicationContext());
        displayer.DisplayData();

        return root;
    }
}