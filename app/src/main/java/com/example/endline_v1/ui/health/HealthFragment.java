package com.example.endline_v1.ui.health;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.endline_v1.util.DisplayDataFromFirebase;
import com.example.endline_v1.R;

public class HealthFragment extends Fragment {

    private HealthViewModel healthViewModel;
    private RecyclerView recyclerView;
    private Activity activity;
    private DisplayDataFromFirebase displayer;
    private Spinner spinner_filter_health;
    private String filter_index;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        healthViewModel =
                new ViewModelProvider(this).get(HealthViewModel.class);
        View root = inflater.inflate(R.layout.fragment_health, container, false);

        spinner_filter_health = (Spinner) root.findViewById(R.id.spinner_filter_health);
        spinner_filter_health.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter_index = parent.getItemAtPosition(position).toString();
                displayer = new DisplayDataFromFirebase("건강", filter_index, recyclerView, activity.getApplicationContext());
                displayer.DisplayData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner_filter_health.setSelection(0);
                filter_index = "등록일자순";
            }
        });

        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView_health);
        DisplayDataFromFirebase displayer = new DisplayDataFromFirebase("건강", recyclerView, activity.getApplicationContext());
        displayer.DisplayData();

        return root;
    }
}