package com.foodgeene.splashScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;

import com.foodgeene.MainActivity;
import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.login.LoginActivity;
import com.foodgeene.scanner.ScannerActivity;
import com.foodgeene.success.SuccessActivity;
import com.foodgeene.welcomescreen.Welcome;

import java.io.IOException;
import java.util.Objects;

import static com.paytm.pgsdk.easypay.manager.PaytmAssist.getContext;

public class SplashScreen extends AppCompatActivity {
    private static final int GRANT_PERM = 1;
    Handler handler;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        checkLocationPermission();
        //sessionManager
        sessionManager = new SessionManager(this);

        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sessionManager.isLoggin()){

                    Intent intent=new Intent(SplashScreen.this, Welcome.class);
                    startActivity(intent);
                    finish();
                }
                else {

                    Intent intent=new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },2000);
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(SplashScreen.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashScreen.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(SplashScreen.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        GRANT_PERM);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
        }


}
