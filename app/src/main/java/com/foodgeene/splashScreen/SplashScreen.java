package com.foodgeene.splashScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.foodgeene.MainActivity;
import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.login.LoginActivity;
import com.foodgeene.scanner.ScannerActivity;
import com.foodgeene.success.SuccessActivity;
import com.foodgeene.welcomescreen.Welcome;

public class SplashScreen extends AppCompatActivity {
    Handler handler;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
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

                    Intent intent=new Intent(SplashScreen.this, Welcome.class);
                    startActivity(intent);
                    finish();
                }
            }
        },2000);
    }
}
