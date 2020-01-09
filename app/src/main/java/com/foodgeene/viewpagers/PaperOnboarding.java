package com.foodgeene.viewpagers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.login.LoginActivity;
import com.ramotion.paperonboarding.PaperOnboardingEngine;
import com.ramotion.paperonboarding.PaperOnboardingPage;
import com.ramotion.paperonboarding.listeners.PaperOnboardingOnChangeListener;
import com.ramotion.paperonboarding.listeners.PaperOnboardingOnRightOutListener;

import java.util.ArrayList;

public class PaperOnboarding extends AppCompatActivity {

    Button skip, done;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager  = new SessionManager(this);
        if(restorePrefData()){
            Intent intent = new Intent(PaperOnboarding.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        }


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_paper_onboarding);

        skip = findViewById(R.id.skipButton);
        done = findViewById(R.id.doneButton);
        skip.setOnClickListener(v -> {

            Intent intent = new Intent(PaperOnboarding.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        done.setOnClickListener(v -> {
            Intent intent = new Intent(PaperOnboarding.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        PaperOnboardingEngine engine = new PaperOnboardingEngine(findViewById(R.id.onboardingRootView), getDataForOnboarding(), getApplicationContext());
        engine.setOnChangeListener((i, i1) -> {


        });


        engine.setOnRightOutListener(() -> {

            sessionManager.setFirstTimeLaunch(true);
        });

        savePrefsData();


    }

    private ArrayList<PaperOnboardingPage> getDataForOnboarding() {
        PaperOnboardingPage scr1 = new PaperOnboardingPage("Self Serviced ", "All hotels and hostels are sorted by hospitality rating",
                Color.parseColor("#FFE0B2"), R.drawable.hotelsdr, R.drawable.ic_account_circle_black_24dp);
        PaperOnboardingPage scr2 = new PaperOnboardingPage("Verified Taste", "We carefully verify all the restaurants before adding them into the app",
                Color.parseColor("#8BC34A"), R.drawable.cutlerydr, R.drawable.ic_account_circle_black_24dp);
        PaperOnboardingPage scr3 = new PaperOnboardingPage("Everything Covered", "All local stores are categorized for your convenience",
                Color.parseColor("#FFCDD2"), R.drawable.soup, R.drawable.ic_account_circle_black_24dp);

        ArrayList<PaperOnboardingPage> elements = new ArrayList<>();
        elements.add(scr1);
        elements.add(scr2);
        elements.add(scr3);
        return elements;


    }

    private boolean restorePrefData() {


        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isIntroOpnend",false);
        return  isIntroActivityOpnendBefore;



    }


    private void savePrefsData() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnend",true);
        editor.commit();


    }
}
