package com.foodgeene;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.cart.Orders;
import com.foodgeene.home.Home;
import com.foodgeene.profile.Profile;
import com.foodgeene.rewards.Rewards;
import com.foodgeene.rewardsfragment.RewardsFragment;
import com.foodgeene.scanner.ScannerActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity  {
    SessionManager sessionManager;
    Button logoutButton;
    String Current_Tab="Home";
    final int requestLocation = 1001;
    private final static String TAG = "MainActivity";
    public final static String PREFS = "PrefsFile";
    
    BottomNavigationView bottomNavigationView;
    private LocationManager locationManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        checkLocationPerm();
        sessionManager = new SessionManager(this);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
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
                case R.id.nav_rewards:

                if(Current_Tab.equals("Rewards")){
                }
                else{
                    Current_Tab="Rewards";
                    loadFragment(new RewardsFragment());

                }
                break;
            }
            return true;
        });
        loadFragment(new Home());
    }



    private void checkLocationPerm() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        !=PackageManager.PERMISSION_GRANTED){

            return;

        }

//        Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
//        onLocationChanged(location);
    }

    private void onLocationChanged(Location location) {

//        double longi = location.getLongitude();
//        double lati = location.getLatitude();

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
            new AlertDialog.Builder(this)
                    .setMessage("Thank you for using FoodQ & come back again")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> finish())
                    .setNegativeButton("No", null)
                    .show();
        }
        else{
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
            Current_Tab="Home";
            Fragment fragment;
            fragment = new Home();
            loadFragment(fragment);

        }


    }

//    public void showDailog() {
//
//        new AppRatingDialog.Builder()
//                .setPositiveButtonText("Submit")
//                .setNegativeButtonText("Cancel")
//                .setNoteDescriptions(Arrays.asList("Bad", "Good", "Delicious"))
//                .setTitle("Rate this order")
//                .setDescription("Please rate your order")
//                .setCommentInputEnabled(true)
//                .setDefaultComment("Food is life")
//                .setWindowAnimation(R.style.MyDialogFadeAnimation)
//                .setCancelable(false)
//                .setCanceledOnTouchOutside(false)
//                .create(MainActivity.this)
//                .setTargetFragment(new Fragment(), 1)
//                .show();
//
//
//    }
//
//
//    @Override
//    public void onNegativeButtonClicked() {
//
//    }
//
//    @Override
//    public void onNeutralButtonClicked() {
//
//    }
//
//    @Override
//    public void onPositiveButtonClicked(int i, @NotNull String s) {
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        new AlertDialog.Builder(this)
//                .setMessage("Are you sure you want to exit?")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        MainActivity.super.onBackPressed();
//                    }
//                })
//                .setNegativeButton("No", null)
//                .show();
//    }
}
