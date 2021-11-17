package com.example.endline_v1.ui.all;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.endline_v1.DisplayDataFromFirebase;
import com.example.endline_v1.R;

public class AllFragment extends Fragment {

    private AllViewModel allViewModel;
    private RecyclerView recyclerView;
    private Activity activity;
    private DisplayDataFromFirebase displayer;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        allViewModel =
                new ViewModelProvider(this).get(AllViewModel.class);
        View root = inflater.inflate(R.layout.fragment_all, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView_all);
        displayer = new DisplayDataFromFirebase("All", recyclerView, activity.getApplicationContext());
        displayer.DisplayData();

        return root;
    }
}