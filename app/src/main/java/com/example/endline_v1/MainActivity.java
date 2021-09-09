package com.example.endline_v1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
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
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

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
    private SignInButton btn_google;                    //Login Button
    private FirebaseAuth auth;                          //Auth
    private GoogleApiClient googleApiClient;            //Google API Client
    private static final int REQ_SIGN_GOOGLE = 100;     //Result Code of Google Login

    private TextView tv_result;     //User ID
    private ImageView iv_profile;   //User Profile Photo

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
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        //R.id.nav_gallery, R.id.nav_slideshow,
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_beauty, R.id.nav_food,
                R.id.nav_health, R.id.nav_medical, R.id.nav_login)
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

        auth = FirebaseAuth.getInstance();      //Get Auth Instance

        btn_google = navigationView.getHeaderView(0).findViewById(R.id.btn_google);
        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);      //Go to Google Login Interface
                startActivityForResult(intent, REQ_SIGN_GOOGLE);
            }
        });

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

        if(requestCode == REQ_SIGN_GOOGLE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){     //Check Auth State
                GoogleSignInAccount account = result.getSignInAccount();    //Get Every Login Information(Photo, ID, Email, PW...)
                resultLogin(account);   //Print Login Information
            }
        }
    }

    private void resultLogin(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {    //Check Login State
                        if(task.isSuccessful()){    //Login Success
                            Toast.makeText(MainActivity.this, "Success Login", Toast.LENGTH_SHORT).show();
                            btn_google.setVisibility(View.GONE);
                            tv_result = (TextView) findViewById(R.id.tv_id);
                            iv_profile = (ImageView) findViewById(R.id.iv_profile);

                            tv_result.setText(account.getDisplayName().toString());
                            Glide.with(MainActivity.this).load(String.valueOf(account.getPhotoUrl())).into(iv_profile);
                        }else{  //Login Fail
                            Toast.makeText(MainActivity.this, "Fail Login", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void resultLogout(){
        btn_google.setVisibility(View.VISIBLE);
        tv_result = (TextView) findViewById(R.id.tv_id);
        iv_profile = (ImageView) findViewById(R.id.iv_profile);

        tv_result.setVisibility(View.GONE);
        iv_profile.setVisibility(View.GONE);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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