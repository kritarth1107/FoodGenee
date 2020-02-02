package com.foodgeene;

import android.content.Context;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationUpdatesComponent {

    private static final String TAG = LocationUpdatesComponent.class.getSimpleName();

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10 * 1000;

    /**
     * The fastest rate for active location updates. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    private LocationRequest mLocationRequest;

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Callback for changes in location.
     */
    private LocationCallback mLocationCallback;

    /**
     * The current location.
     */
    private Location mLocation;

    public ILocationProvider iLocationProvider;
    private GoogleApiClient googleApiClient;

    public LocationUpdatesComponent(ILocationProvider iLocationProvider) {
        this.iLocationProvider = iLocationProvider;
    }

    /**
     * create first time to initialize the location components
     *
     * @param context
     */
    public void onCreate(Context context) {
        Log.i(TAG, "created...............");
        createLocationRequest();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                if (locationResult == null) {
                    getLastLocation();
                    return;
                }
                for (Location location : locationResult.getLocations())
                    onNewLocation(location);
            }
        };

        // ;
    }

    /**
     * start location updates
     */
    public void onStart() {
        Log.i(TAG, "onStart ");
        //hey request for location updates
        requestLocationUpdates();
    }

    /**
     * remove location updates
     */
    public void onStop() {
        Log.i(TAG, "onStop....");
        removeLocationUpdates();
    }


    public void requestLocationUpdates() {

        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback, Looper.myLooper());
        } catch (Exception unlikely) {

        }
    }

    public void removeLocationUpdates() {

        try {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);

        } catch (SecurityException unlikely) {
//            Utils.setRequestingLocationUpdates(this, true);
            Log.e(TAG, "Lost location permission. Could not remove updates. " + unlikely);
        }
    }

    /**
     * get last location
     */
    public void getLastLocation() {
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLocation = task.getResult();

//                                Toast.makeText(getApplicationContext(), "" + mLocation, Toast.LENGTH_SHORT).show();
                            onNewLocation(mLocation);
                        } else {
                            Log.w(TAG, "Failed to get location.");
                        }
                    });

        } catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission." + unlikely);

        }

    }

    private void onNewLocation(Location location) {


        mLocation = location;
        if (this.iLocationProvider != null) {
            this.iLocationProvider.onLocationUpdate(mLocation);
        }
    }

    /**
     * Sets the location request parameters.
     */
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setMaxWaitTime(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
    }

    /**
     * implements this interface to get call back of location changes
     */
    public interface ILocationProvider {
        void onLocationUpdate(Location location);
    }


}
