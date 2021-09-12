package com.example.endline_v1.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.endline_v1.MainActivity;
import com.example.endline_v1.R;

public class ProfileFragment extends Fragment {

    MainActivity mainActivity;
    private ProfileViewModel profileViewModel;
    public Button btn_logout;
    Button btn_updateProfile;
    ImageView iv_profilePhoto;
    EditText et_displayName;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        final TextView tv_loginStatus = root.findViewById(R.id.tv_loginStatus);
        profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tv_loginStatus.setText(s);
            }
        });

        et_displayName = (EditText) root.findViewById(R.id.et_displayName);
        btn_logout = (Button) root.findViewById(R.id.btn_logout);
        if(!mainActivity.isLogin){  //none Login state
            btn_logout.setVisibility(View.GONE);
            et_displayName.setText("");
        }else{  //login state
            et_displayName.setText(mainActivity.displayName);
        }
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.OnFragmentChange(1);
                Toast.makeText(getActivity(), "로그아웃", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);

                et_displayName.setText("");
            }
        });



//        get profile photo url from mainActivity, but get null reference, why?
//        Glide.with(mainActivity.getApplicationContext()).load(Uri.parse(mainActivity.profilePhotoUrl)).into(iv_profilePhoto);

        return root;
    }
}