package com.foodgeene.home;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.foodgeene.LocationUpdatesComponent;
import com.foodgeene.MainActivity;
import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.allhotels.ui.AllRestraunts;
import com.foodgeene.home.brandlist.BrandAdapter;
import com.foodgeene.home.brandlist.brandmodel.Bannerlist;
import com.foodgeene.home.brandlist.brandmodel.Brand;
import com.foodgeene.home.hometwo.FlipperAdapter;
import com.foodgeene.home.hometwo.HomeTwoAdapter;
import com.foodgeene.home.hometwo.models.HomeTwoModel;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import network.ConnectivityReceiver;
import network.FoodGeneeAPI;
import network.MyApplication;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment implements LocationUpdatesComponent.ILocationProvider, ConnectivityReceiver.ConnectivityReceiverListener {
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    TextView locationSub;
    RecyclerView merchantListReycler, merchantTwoRecycler;
    HomeAdapter homeAdapter;
    ShimmerFrameLayout shimmerFrameLayout, shimmerFrameLayout2, shimmerFrameLayout3;
    HomeTwoAdapter homeTwoAdapter;
    BrandAdapter brandAdapter;
    LinearLayoutManager layoutManager;
    public static TextView locationNew;
    ImageView mTvMerch, mTvSecond;
    public static double longi = 0.0;
    public static double lati = 0.0;
    private AdapterViewFlipper adapterViewFlipper;
    TextView viewAll;
    boolean isOnline;
    Location mLastLocation;
    public static final int LOCATION_REQUEST = 101;
    private LocationUpdatesComponent locationUpdateService;
    private FusedLocationProviderClient fusedLocationClient;
    LocationManager locationManager;
    LocationRequest locationRequest;
    LocationSettingsRequest.Builder locationSettingsRequest;
    PendingResult<LocationSettingsResult> pendingResult;
    GoogleApiClient googleApiClient;
    private static final int GRANT_PERM = 1;
    public static Dialog dialog;

    public Home() {
        // Required empty public constructor
    }

    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static GoogleApiClient mGoogleApiClient;
    private static final int LOCATION_PERMISSION_ID = 1001;

    private void onLocationUpdates() {
        locationUpdateService = new LocationUpdatesComponent(this);
        locationUpdateService.onCreate(viewAll.getContext());
        locationUpdateService.onStart();
        locationUpdateService.getLastLocation();
    }

    MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.new_home, container, false);
        locationSub = rootView.findViewById(R.id.locationCity);
        merchantListReycler = rootView.findViewById(R.id.mercRecycler);
        merchantTwoRecycler = rootView.findViewById(R.id.secondRecycler);
        adapterViewFlipper = rootView.findViewById(R.id.brandRecycler);
        mTvMerch = rootView.findViewById(R.id.tv_merch);
        mTvSecond = rootView.findViewById(R.id.tv_second);
        locationNew = rootView.findViewById(R.id.location);
        viewAll = rootView.findViewById(R.id.viewallButton);
        isOnline = ConnectivityReceiver.isConnected();
        mainActivity = (MainActivity) getActivity();
        viewAll.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), AllRestraunts.class));
        });

        //shimmer
        shimmerFrameLayout = rootView.findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout2 = rootView.findViewById(R.id.shimmer_view_container2);
        shimmerFrameLayout3 = rootView.findViewById(R.id.shimmer_view_container3);
        //checkLocationPermission();


        initGoogleAPIClient();
        checkPermissions();
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {


        } else {

        }


        //getCurrentLocation();


        locationNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocations();
            }
        });

        return rootView;
    }


    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(getActivity(),
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


    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);

    }

    private void getLocations() {

        try {
            FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
            SessionManager sessionManager = new SessionManager(getContext());

            HashMap<String, String> user = sessionManager.getUserDetail();

            String userToken = user.get(sessionManager.USER_ID);


            Call<HomeMerchantModel> call = foodGeneeAPI.location("searchlocations", userToken, "application/x-www-form-urlencoded");
            call.enqueue(new Callback<HomeMerchantModel>() {
                @Override
                public void onResponse(Call<HomeMerchantModel> call, Response<HomeMerchantModel> response) {


                    HomeMerchantModel homeMerchantModel = response.body();
                    List<HomeMerchantModel> locationlist = homeMerchantModel.getLocationlist();
                    if (homeMerchantModel.getStatus() == 1) {

                        if (locationlist.size() > 0) {
                            List<String> cities = new ArrayList<String>();
                            for (int i = 0; i < locationlist.size(); i++) {
                                if (cities.size() > 0) {
                                    if (cities.contains(locationlist.get(i).getCity())) {

                                    } else {
                                        cities.add(locationlist.get(i).getCity());
                                    }
                                } else cities.add(locationlist.get(i).getCity());
                            }
                            dialog = new Dialog(getActivity(), R.style.Full_Screen_Dialog);
                            dialog.setCancelable(true);
                            dialog.setContentView(R.layout.dialog_location);
                            ImageView back = dialog.findViewById(R.id.d_back);
                            back.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                            List<HomeMerchantModel> location = new ArrayList<HomeMerchantModel>();
                            for (int j = 0; j < locationlist.size(); j++) {
                                if (!locationlist.get(j).getLatitude().equalsIgnoreCase(""))
                                    location.add(locationlist.get(j));

                            }
                            SearchView editsearch = dialog.findViewById(R.id.search);

                            RecyclerView recyclerView = dialog.findViewById(R.id.recycler);
                            AdapterRe adapterRe = new AdapterRe(getActivity(), location);
                            recyclerView.setAdapter(adapterRe);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                            adapterRe.setOnCheckedListener(new RecyclerItemClickListener() {
                                @Override
                                public void onItemClickListener(int position) {
                                    lati = Double.valueOf(location.get(position).getLatitude());
                                    longi = Double.valueOf(location.get(position).getLongitude());
                                    locationNew.setText(location.get(position).getLocation());
                                    dialog.dismiss();
                                    if (isOnline)
                                        setupRecyclerView();
                                    else
                                        Toast.makeText(getActivity(), "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();

                                }
                            });
                            editsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                @Override
                                public boolean onQueryTextSubmit(String query) {
                                    return false;
                                }

                                @Override
                                public boolean onQueryTextChange(String newText) {
                                    String text = newText;
                                    adapterRe.filter(text);
                                    return false;
                                }
                            });

                /*            Spinner spinner = dialog.findViewById(R.id.sp_city);
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, cities);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(dataAdapter);
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {



                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });*/


                            dialog.show();

                            // Log.e("location size",""+locationlist.size());

                        }
                    } else
                        Toast.makeText(getContext(), "Invalid Response", Toast.LENGTH_SHORT).show();


//                else{
//
//                    Toast.makeText(getContext(), homeMerchantModel.getStatus(), Toast.LENGTH_SHORT).show();
//                }

                }

                @Override
                public void onFailure(Call<HomeMerchantModel> call, Throwable t) {
                    Toast.makeText(getContext(), "Try again", Toast.LENGTH_SHORT).show();


                }
            });


            Call<HomeTwoModel> newCall = foodGeneeAPI.getRecomm("recommendlist", String.valueOf(Home.lati), String.valueOf(Home.longi), userToken, "application/x-www-form-urlencoded");
            newCall.enqueue(new Callback<HomeTwoModel>() {
                @Override
                public void onResponse(Call<HomeTwoModel> call, Response<HomeTwoModel> response) {
                    HomeTwoModel homeTwoModel = response.body();
                    List<com.foodgeene.home.hometwo.models.Merchantlist> newlist = homeTwoModel.getMerchantlist();

                    if (homeTwoModel.getStatus() == 1) {
                        if (newlist.size() > 0) {
                            mTvSecond.setVisibility(View.GONE);
                            merchantTwoRecycler.setVisibility(View.VISIBLE);
                            homeTwoAdapter = new HomeTwoAdapter(newlist, getContext());
                            merchantTwoRecycler.setAdapter(homeTwoAdapter);
                            layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
                            merchantTwoRecycler.setLayoutManager(layoutManager);

                        } else {
                            mTvSecond.setVisibility(View.VISIBLE);
                            merchantTwoRecycler.setVisibility(View.GONE);
                        }


                        shimmerFrameLayout2.setVisibility(View.GONE);
                        shimmerFrameLayout2.stopShimmerAnimation();


                    } else if (homeTwoModel.getStatus() == 0) {

                        shimmerFrameLayout2.setVisibility(View.GONE);
                        shimmerFrameLayout2.stopShimmerAnimation();

                    }


                }

                @Override
                public void onFailure(Call<HomeTwoModel> call, Throwable t) {

                    shimmerFrameLayout2.setVisibility(View.GONE);
                    shimmerFrameLayout2.stopShimmerAnimation();
                    mTvSecond.setVisibility(View.VISIBLE);
                    merchantTwoRecycler.setVisibility(View.GONE);
                }
            });


            Call<Brand> brand = foodGeneeAPI.getBrandList("bannerlist", String.valueOf(Home.lati), String.valueOf(Home.longi), userToken, "application/x-www-form-urlencoded");
            brand.enqueue(new Callback<Brand>() {
                @Override
                public void onResponse(Call<Brand> call, Response<Brand> response) {

                    Brand brand1 = response.body();
                    List<Bannerlist> bannerlists = brand1.getBannerlist();

                    if(bannerlists!=null){
                        if(bannerlists.size()>0){
                            FlipperAdapter flipper = new FlipperAdapter(getContext(), bannerlists);
                            adapterViewFlipper.setAdapter(flipper);
                            adapterViewFlipper.setFlipInterval(3000);
                            adapterViewFlipper.startFlipping();

                        }
                    }
                    shimmerFrameLayout.setVisibility(View.GONE);
                    shimmerFrameLayout.stopShimmerAnimation();

//                    brandAdapter = new BrandAdapter(bannerlists, getContext());
//                    layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
//                    brandRecycler.setLayoutManager(layoutManager);
//                    brandRecycler.setAdapter(brandAdapter);
//                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(brandRecycler.getContext(),
//                            layoutManager.getOrientation());
//                    brandRecycler.addItemDecoration(dividerItemDecoration);





                }

                @Override
                public void onFailure(Call<Brand> call, Throwable t) {
                    shimmerFrameLayout.setVisibility(View.GONE);
                    shimmerFrameLayout.stopShimmerAnimation();

                }
            });

        } catch (Exception e) {
            //  Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
            shimmerFrameLayout.setVisibility(View.GONE);
            shimmerFrameLayout.stopShimmerAnimation();
        }


    }

    public void setupRecyclerView() {

        try {
            FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
            SessionManager sessionManager = new SessionManager(getContext());

            HashMap<String, String> user = sessionManager.getUserDetail();

            String userToken = user.get(sessionManager.USER_ID);

            shimmerFrameLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.startShimmerAnimation();
            shimmerFrameLayout2.setVisibility(View.VISIBLE);
            shimmerFrameLayout2.startShimmerAnimation();
            shimmerFrameLayout3.setVisibility(View.VISIBLE);
            shimmerFrameLayout3.startShimmerAnimation();

            Call<HomeMerchantModel> call = foodGeneeAPI.merchantList("merchants", String.valueOf(Home.lati), String.valueOf(Home.longi), userToken, "application/x-www-form-urlencoded");
            call.enqueue(new Callback<HomeMerchantModel>() {
                @Override
                public void onResponse(Call<HomeMerchantModel> call, Response<HomeMerchantModel> response) {


                    HomeMerchantModel homeMerchantModel = response.body();
                    List<Merchantlist> retrievedMerchantList = homeMerchantModel.getMerchantlist();

                    if (homeMerchantModel.getStatus() == 1) {
                        if (retrievedMerchantList.size() > 0) {
                            merchantListReycler.setVisibility(View.VISIBLE);
                            homeAdapter = new HomeAdapter(retrievedMerchantList, getContext());
                            layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);

                            merchantListReycler.setAdapter(homeAdapter);
                            merchantListReycler.setLayoutManager(layoutManager);
                            mTvMerch.setVisibility(View.GONE);
                        } else {
                            mTvMerch.setVisibility(View.VISIBLE);
                            merchantListReycler.setVisibility(View.GONE);
                        }


                        shimmerFrameLayout3.setVisibility(View.GONE);
                        shimmerFrameLayout3.stopShimmerAnimation();
                    } else if (homeMerchantModel.getStatus() == 0) {
                        mTvMerch.setVisibility(View.VISIBLE);
                        merchantListReycler.setVisibility(View.GONE);
                        // Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        shimmerFrameLayout3.setVisibility(View.GONE);
                        shimmerFrameLayout3.stopShimmerAnimation();
                        mTvSecond.setVisibility(View.VISIBLE);
                        merchantTwoRecycler.setVisibility(View.GONE);

                    }
//                else{
//
//                    Toast.makeText(getContext(), homeMerchantModel.getStatus(), Toast.LENGTH_SHORT).show();
//                }

                }

                @Override
                public void onFailure(Call<HomeMerchantModel> call, Throwable t) {
                    Toast.makeText(getContext(), "Try again", Toast.LENGTH_SHORT).show();
                    shimmerFrameLayout3.setVisibility(View.GONE);
                    shimmerFrameLayout3.stopShimmerAnimation();
                    mTvSecond.setVisibility(View.VISIBLE);
                    merchantTwoRecycler.setVisibility(View.GONE);


                }
            });


            Call<HomeTwoModel> newCall = foodGeneeAPI.getRecomm("recommendlist", String.valueOf(Home.lati), String.valueOf(Home.longi), userToken, "application/x-www-form-urlencoded");
            newCall.enqueue(new Callback<HomeTwoModel>() {
                @Override
                public void onResponse(Call<HomeTwoModel> call, Response<HomeTwoModel> response) {
                    HomeTwoModel homeTwoModel = response.body();
                    List<com.foodgeene.home.hometwo.models.Merchantlist> newlist = homeTwoModel.getMerchantlist();

                    if (homeTwoModel.getStatus() == 1) {
                        if (newlist.size() > 0) {
                            mTvSecond.setVisibility(View.GONE);
                            merchantTwoRecycler.setVisibility(View.VISIBLE);
                            homeTwoAdapter = new HomeTwoAdapter(newlist, getContext());
                            merchantTwoRecycler.setAdapter(homeTwoAdapter);
                            layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
                            merchantTwoRecycler.setLayoutManager(layoutManager);

                        } else {
                            mTvSecond.setVisibility(View.VISIBLE);
                            merchantTwoRecycler.setVisibility(View.GONE);
                        }


                        shimmerFrameLayout2.setVisibility(View.GONE);
                        shimmerFrameLayout2.stopShimmerAnimation();


                    } else if (homeTwoModel.getStatus() == 0) {

                        shimmerFrameLayout2.setVisibility(View.GONE);
                        shimmerFrameLayout2.stopShimmerAnimation();

                    }


                }

                @Override
                public void onFailure(Call<HomeTwoModel> call, Throwable t) {

                    shimmerFrameLayout2.setVisibility(View.GONE);
                    shimmerFrameLayout2.stopShimmerAnimation();
                    mTvSecond.setVisibility(View.VISIBLE);
                    merchantTwoRecycler.setVisibility(View.GONE);
                }
            });


            Call<Brand> brand = foodGeneeAPI.getBrandList("bannerlist", String.valueOf(Home.lati), String.valueOf(Home.longi), userToken, "application/x-www-form-urlencoded");
            brand.enqueue(new Callback<Brand>() {
                @Override
                public void onResponse(Call<Brand> call, Response<Brand> response) {

                    Brand brand1 = response.body();
                    List<Bannerlist> bannerlists = brand1.getBannerlist();

                    if(bannerlists!=null){
                        if(bannerlists.size()>0){
                            FlipperAdapter flipper = new FlipperAdapter(getContext(), bannerlists);
                            adapterViewFlipper.setAdapter(flipper);
                            adapterViewFlipper.setFlipInterval(3000);
                            adapterViewFlipper.startFlipping();
                        }
                    }


//                    brandAdapter = new BrandAdapter(bannerlists, getContext());
//                    layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
//                    brandRecycler.setLayoutManager(layoutManager);
//                    brandRecycler.setAdapter(brandAdapter);
//                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(brandRecycler.getContext(),
//                            layoutManager.getOrientation());
//                    brandRecycler.addItemDecoration(dividerItemDecoration);


                    shimmerFrameLayout.setVisibility(View.GONE);
                    shimmerFrameLayout.stopShimmerAnimation();


                }

                @Override
                public void onFailure(Call<Brand> call, Throwable t) {
                    shimmerFrameLayout.setVisibility(View.GONE);
                    shimmerFrameLayout.stopShimmerAnimation();

                }
            });

        } catch (Exception e) {
            //  Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
            shimmerFrameLayout.setVisibility(View.GONE);
            shimmerFrameLayout.stopShimmerAnimation();
        }


    }


    private void locateAddress(Location location) {

        try {
            Geocoder geocoder = new Geocoder(getContext());
            List<Address> addresses = null;
            Log.e("latlng", location.getLatitude() + "," + location.getLongitude());
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            String city = addresses.get(0).getLocality();
            String localAdd = addresses.get(0).getSubLocality();
            Log.e("address", localAdd + addresses.get(0).getAddressLine(0));

            locationNew.setText(localAdd);
            // locationNew.setText(""+addresses.get(0).getAddressLine(0)); //Ayush's map works for sublocality Geocoder API
            locationSub.setText(city);


        } catch (Exception e) {


        }


    }


    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(mainActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            // Request permission
            ActivityCompat.requestPermissions(mainActivity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        } else {
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(mainActivity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }


    /* Initiate Google API Client  */
    private void initGoogleAPIClient() {
        //Without Google API Client Auto Location Dialog will not work
        mGoogleApiClient = new GoogleApiClient.Builder(viewAll.getContext())
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    /* Check Location Permission for Marshmallow Devices */
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(viewAll.getContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)
                requestPermissions();
            else
                showSettingDialog();
        } else
            showSettingDialog();

    }


    /* Show Location Access Dialog */
    private void showSettingDialog() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//Setting priotity of Location request to high
        locationRequest.setInterval(2 * 1000);
        locationRequest.setFastestInterval(1000);//5 sec Time interval for location update
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(viewAll.getContext());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(mainActivity, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...


                onLocationUpdates();
            }
        });
        task.addOnFailureListener(mainActivity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(mainActivity,
                                    REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case RESULT_OK:


                        onLocationUpdates();
                        //startLocationUpdates();
                        break;
                    case RESULT_CANCELED:

                        showSettingDialog();
                        break;
                }
                break;
        }
    }

    @Override
    public void onLocationUpdate(Location location) {
        Log.d("TAG", "" + location.getLongitude());
        if (location != null) {
            locateAddress(location);
            if (isOnline)
                setupRecyclerView();
            else
                Toast.makeText(getActivity(), "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();

            locationUpdateService.onStop();
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }
}