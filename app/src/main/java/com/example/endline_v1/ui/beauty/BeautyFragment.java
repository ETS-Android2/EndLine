package com.example.endline_v1.ui.beauty;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.endline_v1.DisplayDataFromFirebase;
import com.example.endline_v1.R;

public class BeautyFragment extends Fragment {

    private BeautyViewModel beautyViewModel;
    private RecyclerView recyclerView;
    private Activity activity;
    DisplayDataFromFirebase displayer;
    private Spinner spinner_filter_beauty;
    private String filter_index;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        beautyViewModel =
                new ViewModelProvider(this).get(BeautyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_beauty, container, false);

        spinner_filter_beauty = (Spinner) root.findViewById(R.id.spinner_filter_beauty);
        spinner_filter_beauty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter_index = parent.getItemAtPosition(position).toString();
                Toast.makeText(activity.getApplicationContext(), filter_index, Toast.LENGTH_SHORT).show();
                displayer = new DisplayDataFromFirebase("화장품", filter_index, recyclerView, activity.getApplicationContext());
                displayer.DisplayData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner_filter_beauty.setSelection(0);
                filter_index = "등록일자순";
            }
        });

        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView_beauty);
        displayer = new DisplayDataFromFirebase("화장품", recyclerView, activity.getApplicationContext());
        displayer.DisplayData();

        return root;
    }
}