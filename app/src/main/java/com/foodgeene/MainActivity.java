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
import com.foodgeene.cart.Orders;
import com.foodgeene.home.Home;
import com.foodgeene.profile.Profile;
import com.foodgeene.scanner.ScannerActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    SessionManager sessionManager;
    Button logoutButton;
    String Current_Tab="Home";
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager = new SessionManager(this);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        if(Current_Tab.equals("Home")){
                        }
                        else{
                            Current_Tab="Home";
                            loadFragment(new Home());

                        }
                        break;
                    case R.id.nav_scan:
                        startActivity(new Intent(getApplicationContext(), ScannerActivity.class));
                        break;
                    case R.id.nav_cart:
                        if(Current_Tab.equals("Cart")){
                        }
                        else{
                            Current_Tab="Cart";
                            loadFragment(new Orders());

                        }
                        break;
                    case R.id.nav_profile:
                        if(Current_Tab.equals("Profile")){
                        }
                        else{
                            Current_Tab="Profile";
                            loadFragment(new Profile());
                        }
                        break;

                }
                return true;
            }
        });
        loadFragment(new Home());
    }

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
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
            Current_Tab="Home";
            Fragment fragment;
            fragment = new Home();
            loadFragment(fragment);

        }
    }
}
