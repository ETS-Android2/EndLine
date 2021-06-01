package com.example.endline_v1.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.endline_v1.MainActivity;
import com.example.endline_v1.R;

public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;
    private Button btn_logout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        loginViewModel =
                new ViewModelProvider(this).get(LoginViewModel.class);
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        final TextView textView = root.findViewById(R.id.text_login);
        loginViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        btn_logout = root.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(), "로그아웃되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }
}