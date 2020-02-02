package com.foodgeene.splashScreen;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;

import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.viewpagers.PaperOnboarding;
import com.foodgeene.welcomescreen.Welcome;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SplashScreen extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {
    private static final int GRANT_PERM = 1;
    Handler handler;
    SessionManager sessionManager;
 //   ImageView gif;


    public static final int REQUEST_LOCATION=001;

    GoogleApiClient googleApiClient;

    LocationManager locationManager;
    LocationRequest locationRequest;
    LocationSettingsRequest.Builder locationSettingsRequest;
    Context context;


    PendingResult<LocationSettingsResult> pendingResult;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
       // gif=findViewById(R.id.gif);
        checkLocationPermission();
        //sessionManager
        sessionManager = new SessionManager(this);

        context = this;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            NextActvity();
           // Toast.makeText(this, "Gps is Enabled", Toast.LENGTH_SHORT).show();

        } else {
            mEnableGps();
        }



    }

    private void NextActvity() {

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sessionManager.isLoggin()) {

                    Intent intent = new Intent(SplashScreen.this, Welcome.class);
                    startActivity(intent);
                    finish();
                } else {

                    Intent intent = new Intent(SplashScreen.this, PaperOnboarding.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 1000);
    }

    public void mEnableGps() {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
        mLocationSetting();
    }

    public void mLocationSetting() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1 * 1000);
        locationRequest.setFastestInterval(1 * 1000);

        locationSettingsRequest = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        mResult();

    }

    public void mResult() {
        pendingResult = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, locationSettingsRequest.build());
        pendingResult.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                Status status = locationSettingsResult.getStatus();


                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        NextActvity();

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {

                            status.startResolutionForResult(SplashScreen.this, REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.


                        break;
                }
            }

        });
    }



    //callback method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        //Toast.makeText(context, "Gps enabled", Toast.LENGTH_SHORT).show();
                        NextActvity();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        mEnableGps();
                        //Toast.makeText(context, "Please enable your Gps ", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                break;
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
