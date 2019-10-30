package com.foodgeene;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.home.Home;
import com.foodgeene.profile.Profile;
import com.foodgeene.scanner.ScannerActivity;
import com.foodgeene.search.Search;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    SessionManager sessionManager;
    Button logoutButton;
    String Current_Tab="Home";
    BottomNavigationView navigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager = new SessionManager(this);
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Fragment fragment;
        fragment = new Home();
        loadFragment(fragment);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.nav_home:
                    if(Current_Tab.equals("Home")){
                    }
                    else{
                        Current_Tab="Home";
                    fragment = new Home();
                    loadFragment(fragment);

                    }
                    break;
                case R.id.nav_scan:
                    startActivity(new Intent(getApplicationContext(), ScannerActivity.class));
                    break;
                case R.id.nav_cart:
                    break;
                case R.id.nav_profile:
                    Current_Tab="Profile";
                    fragment = new Profile();
                    loadFragment(fragment);
                    break;

            }
            return false;
        }
    };


    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if(Current_Tab.equals("Home")){
            finish();
        }
        else{
            Current_Tab="Home";
            Fragment fragment;
            fragment = new Home();
            loadFragment(fragment);

        }
    }
}
