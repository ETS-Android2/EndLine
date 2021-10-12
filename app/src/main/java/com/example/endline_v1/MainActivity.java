package com.example.endline_v1;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.google.firebase.iid.FirebaseInstanceId;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.net.HttpURLConnection;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, FirebaseAuth.AuthStateListener {

    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth auth; //Auth
    FirebaseUser user;

    private TextView tv_result;     //User ID
    private ImageView iv_profile;   //User Profile Photo

    public static String displayName = "";
    public static String profilePhotoUrl = "";
    public static boolean isLogin = false;

    private long backBtnTime = 0;

    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
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

        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("TOKEN", token);
        ConnectServerTask connectServerTask = new ConnectServerTask();
        connectServerTask.execute("http://192.168.0.11:3000/register", token);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(this);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if(firebaseAuth.getCurrentUser() == null){      //non-Login
            Log.d("AUTH STATE", "Main fail");
            startActivity(new Intent(this, LoadActivity.class));
            finish();
        }else{      //login
            Log.d("AUTH STATE", firebaseAuth.getUid());
            resultLogin(firebaseAuth.getCurrentUser());
        }
    }

    //change login state
    public boolean toggleIsSignIn(){
        isLogin = !isLogin;
        return isLogin;
    }

    //set user profile
    public void resultLogin(FirebaseUser user) {
        tv_result = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_id);
        iv_profile = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.iv_profile);

        tv_result.setText(user.getDisplayName());
        Glide.with(MainActivity.this).load(user.getPhotoUrl()).into(iv_profile);

        toggleIsSignIn();
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