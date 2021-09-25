package com.example.endline_v1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth auth;                          //Auth
    private GoogleApiClient googleApiClient;            //Google API Client
    private static final int REQ_SIGN_GOOGLE = 100;     //Result Code of Google Login

    private TextView tv_result;     //User ID
    private ImageView iv_profile;   //User Profile Photo

    public static String sdisplayName = "";
    public static String profilePhotoUrl = "";
    public static boolean isLogin = false;

    public Intent accountIntent;

    private long backBtnTime = 0;

    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ScanBarCode.class));
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        //R.id.nav_gallery, R.id.nav_slideshow,
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_beauty, R.id.nav_food,
                R.id.nav_health, R.id.nav_medical, R.id.nav_profile)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        accountIntent = getIntent();
        auth = FirebaseAuth.getInstance();      //Get Auth Instance
        FirebaseUser user =  auth.getCurrentUser();
        if(user != null){
            if(user.getDisplayName() == null){
                Log.d("DisplayName", "start");
                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName("Set Display Name").build();
                user.updateProfile(profileChangeRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("update DisplayName", "Success");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        Log.e("update DisplayName", "fail");
                    }
                });
            }
            Log.d("user info", user.getDisplayName().toString());
            resultLogin(user);
        }
    }

    //change login state
    public boolean toggleIsSignIn(){
        isLogin = !isLogin;
        return isLogin;
    }

    public void OnFragmentChange(int index){
        if(index == 1){
            signOut();
        }
    }

    private void signOut() {
        googleApiClient.connect();
        googleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                auth.signOut();
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()){
                            Log.v("LogOut Event", "Success");
                            Toast.makeText(getApplicationContext(), "로그아웃성공", Toast.LENGTH_SHORT).show();
                            resultLogout();
                        }
                        else{
                            Log.v("LogOut Event", "Fail");
                            Toast.makeText(getApplicationContext(), "로그아웃실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            @Override
            public void onConnectionSuspended(int i) {
                Log.v("LogOut Event", "Suspended");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    //set user profile
    public void resultLogin(FirebaseUser user) {
//        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
//        auth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {    //Check Login State
//                        if(task.isSuccessful()){    //Login Success
//                            tv_result = (TextView) findViewById(R.id.tv_id);
//                            iv_profile = (ImageView) findViewById(R.id.iv_profile);
//
//                            tv_result.setText(account.getDisplayName().toString());
//                            displayName = account.getDisplayName();
//
//                            Glide.with(MainActivity.this).load(String.valueOf(account.getPhotoUrl())).into(iv_profile);
//                            profilePhotoUrl = account.getPhotoUrl().toString();
//
//                            toggleIsSignIn();
//                        }else{  //Login Fail
//                            Toast.makeText(MainActivity.this, "Fail Login", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
        tv_result = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_id);
        iv_profile = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.iv_profile);

        Log.d("resultLogin name", sdisplayName);

        tv_result.setText(user.getDisplayName());
        Glide.with(MainActivity.this).load(user.getPhotoUrl()).into(iv_profile);
    }

    //after logout => push to load activity
    private void resultLogout(){
        Intent intent = new Intent(this, LoadActivity.class);
        startActivity(intent);
        finish();
    }

    //back button safety
    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if(0<= gapTime && 2000 >= gapTime){
            super.onBackPressed();
        }else{
            backBtnTime = curTime;
            Toast.makeText(getApplicationContext(), "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다", Toast.LENGTH_SHORT).show();
        }
    }

    //create notification, search menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //notification setting page
        if(item.getItemId() == R.id.action_notification){
            Intent iNotification = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(iNotification);
        }else if(item.getItemId() == R.id.action_search){   //search activity start
            Intent iSearch = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(iSearch);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}