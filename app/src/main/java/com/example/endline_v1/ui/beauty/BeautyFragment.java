package com.example.endline_v1.ui.beauty;

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

import com.example.endline_v1.R;

public class BeautyFragment extends Fragment {

    private BeautyViewModel beautyViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        beautyViewModel =
                new ViewModelProvider(this).get(BeautyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_beauty, container, false);
        final TextView textView = root.findViewById(R.id.text_beauty);
        beautyViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}