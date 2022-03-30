package com.example.endline_v1.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.endline_v1.activity.LoadActivity;
import com.example.endline_v1.activity.MainActivity;
import com.example.endline_v1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ProfileFragment extends Fragment implements FirebaseAuth.AuthStateListener {

    MainActivity mainActivity;
    private ProfileViewModel profileViewModel;
    Button btn_logout, btn_updateProfile;
    ImageView iv_profilePhoto;
    EditText et_displayName;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) getActivity();
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

        auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(this);
        user = auth.getCurrentUser();

        et_displayName = (EditText) root.findViewById(R.id.et_displayName);
        iv_profilePhoto = (ImageView) root.findViewById(R.id.iv_profilePhoto);
        btn_updateProfile = (Button) root.findViewById(R.id.btn_updateProfile);
        btn_logout = (Button) root.findViewById(R.id.btn_logout);

        et_displayName.setText(user.getDisplayName() + "");
        Glide.with(mainActivity.getApplicationContext()).load(user.getPhotoUrl()).into(iv_profilePhoto);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
            }
        });

        btn_updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();
                UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setDisplayName(et_displayName.getText().toString()).build();
                user.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(), "변경되었습니다.", Toast.LENGTH_SHORT).show();
                            mainActivity.resultLogin(user);
                            Log.d("UPDATE NAME", et_displayName.getText().toString());
                        }else{
                            Log.w("UPDATE NAME", "failed");
                        }
                    }
                });
            }
        });

        return root;
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if(firebaseAuth.getCurrentUser() == null){
            Intent intent = new Intent(mainActivity.getApplicationContext(), LoadActivity.class);
            intent.putExtra("signOut", "True");
            startActivity(intent);
            onDetach();
            Log.d("USER", "Log out state");
        }else{
            Log.d("USER", firebaseAuth.getUid() + " : Login");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        auth.removeAuthStateListener(this);
    }
}