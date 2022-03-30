package com.example.endline_v1.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.endline_v1.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;
import java.util.regex.Pattern;

public class LoadActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    SignInButton btn_google;
    GoogleApiClient googleApiClient;
    FirebaseAuth auth;

    private static final int REQ_SIGN_GOOGLE = 100;
    private static final String TAG = "Login Status";

    Button btn_login, btn_join;
    EditText et_email, et_password;
    TextView tv_toggle;

    boolean isNewAccount = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        //firebase auth
        auth = FirebaseAuth.getInstance();

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_join = (Button) findViewById(R.id.btn_join);
        et_email = (EditText) findViewById(R.id.et_email);
        et_password  = (EditText) findViewById(R.id.et_password);
        tv_toggle = (TextView) findViewById(R.id.tv_toggle);

        tv_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNewAccount = !isNewAccount;
                Log.d("isNewAccount", isNewAccount + "");
                toggleButton(isNewAccount);
            }
        });

        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validate = validateUserInfo(et_email.getText().toString(), et_password.getText().toString());
                if(validate){
                    auth.createUserWithEmailAndPassword(et_email.getText().toString(), et_password.getText().toString()).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Log.d("createUser", "Uid => " + auth.getUid());
                                Toast.makeText(getApplicationContext(), "환영합니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();
                            }else if(task.isCanceled()){
                                Toast.makeText(getApplicationContext(), "사용자 회원가입 취소", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validate = validateUserInfo(et_email.getText().toString(), et_password.getText().toString());
                if(validate){
                    auth.signInWithEmailAndPassword(et_email.getText().toString(), et_password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "환영합니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();
                            }else if(task.isCanceled()){
                                Toast.makeText(getApplicationContext(), "사용자 로그인 취소", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    });
                }
            }
        });

        //permission listener
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(LoadActivity.this, "권한 승인", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(LoadActivity.this, "권한 거부", Toast.LENGTH_SHORT).show();
                finish();
            }
        };

        //check permission
        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage("카메라 접근권한이 필요합니다.")
                .setDeniedMessage("거부")
                .setPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CHANGE_NETWORK_STATE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE)
                .check();

        //google sign in option
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        //google login button click function
        btn_google = findViewById(R.id.btn_google);
        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);      //Go to Google Login Interface
                startActivityForResult(intent, REQ_SIGN_GOOGLE);
            }
        });

        Intent intent = getIntent();
        if(!(intent.hasExtra("signOut"))){
            GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
            if(googleSignInAccount != null){
                firebaseAuthWithGoogle(googleSignInAccount);
            }
        }
    }

    private boolean validateUserInfo(String email, String password) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        if(email == null){
            Toast.makeText(this, "이메일을 입력하세요!", Toast.LENGTH_SHORT).show();
            return false;
        }else if(!(pattern.matcher(email).matches())){
            Toast.makeText(this, "올바른 이메일 형식을 입력하세요!", Toast.LENGTH_SHORT).show();
            return false;
        }else if(password == null) {
            Toast.makeText(this, "비밀번호을 입력하세요!", Toast.LENGTH_SHORT).show();
            return false;
        }else if(password.length() < 8){
            Toast.makeText(this, "비밀번호는 8자리 이상입니다!", Toast.LENGTH_LONG).show();
            return false;
        }
        else{
            return true;
        }
    }

    private void toggleButton(boolean isNewAccount) {
        if(isNewAccount){
            btn_login.setVisibility(View.INVISIBLE);
            btn_login.setEnabled(false);
            btn_join.setVisibility(View.VISIBLE);
            btn_join.setEnabled(true);
            tv_toggle.setText("로그인");
        }else{
            btn_join.setVisibility(View.INVISIBLE);
            btn_join.setEnabled(false);
            btn_login.setVisibility(View.VISIBLE);
            btn_login.setEnabled(true);
            tv_toggle.setText("회원가입");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //return from google login pop-up
        if(requestCode == REQ_SIGN_GOOGLE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        ProgressDialog progressDialog = ProgressDialog.show(this, "로그인", "로그인 중입니다.", true);
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "환영합니다", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.w(TAG, "get api client failed");
    }
}
