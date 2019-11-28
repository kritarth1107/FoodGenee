package com.foodgeene.home;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.home.brandlist.BrandAdapter;
import com.foodgeene.home.brandlist.brandmodel.Bannerlist;
import com.foodgeene.home.brandlist.brandmodel.Brand;
import com.foodgeene.home.brandlist.brandmodel.Brandlist;
import com.foodgeene.home.hometwo.FlipperAdapter;
import com.foodgeene.home.hometwo.HomeTwoAdapter;
import com.foodgeene.home.hometwo.models.HomeTwoModel;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import network.FoodGeneeAPI;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment implements LocationListener {

    private static final int GRANT_PERM = 1;
    TextView locationSub;
    RecyclerView merchantListReycler, merchantTwoRecycler, brandRecycler;
    HomeAdapter homeAdapter;
    ShimmerFrameLayout shimmerFrameLayout, shimmerFrameLayout2, shimmerFrameLayout3;
    HomeTwoAdapter homeTwoAdapter;
    BrandAdapter brandAdapter;
    LinearLayoutManager layoutManager;
    TextView locationNew;
    private LocationManager locationManager;
    double longi;
    double lati;
    private AdapterViewFlipper adapterViewFlipper;


    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View rootView = inflater.inflate(R.layout.new_home, container, false);
         locationSub = rootView.findViewById(R.id.locationCity);
        merchantListReycler = rootView.findViewById(R.id.mercRecycler);
        merchantTwoRecycler = rootView.findViewById(R.id.secondRecycler);
        adapterViewFlipper = rootView.findViewById(R.id.brandRecycler);
//        brandRecycler = rootView.findViewById(R.id.brandRecycler);
        locationNew = rootView.findViewById(R.id.location);
        locationManager = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.LOCATION_SERVICE);
        checkLocationPerm();

        //shimmer
        shimmerFrameLayout = rootView.findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout2 = rootView.findViewById(R.id.shimmer_view_container2);
        shimmerFrameLayout3 = rootView.findViewById(R.id.shimmer_view_container3);

        setupRecyclerView();


        return rootView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

//        switch (requestCode){
//
//        }
    }

    private void checkLocationPerm() {
//        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED &&
//
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


//            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION));
            if (ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_COARSE_LOCATION)) {
                Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                onLocationChanged(location);
                try {
                    locateAddress(location);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        GRANT_PERM);
//                Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
//                onLocationChanged(location);
//                try {
//                    locateAddress(location);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }


            }

        } else {

            Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
            onLocationChanged(location);
            try {
                locateAddress(location);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void onLocationChanged(Location location) {

        try {
            lati = location.getLatitude();
            longi = location.getLongitude();

        } catch (Exception e) {

        }


    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {


    }

    private void setupRecyclerView() {

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

            Call<HomeMerchantModel> call = foodGeneeAPI.merchantList("merchants", userToken, "application/x-www-form-urlencoded");
            call.enqueue(new Callback<HomeMerchantModel>() {
                @Override
                public void onResponse(Call<HomeMerchantModel> call, Response<HomeMerchantModel> response) {


                    HomeMerchantModel homeMerchantModel = response.body();
                    List<Merchantlist> retrievedMerchantList = homeMerchantModel.getMerchantlist();

                    if (homeMerchantModel.getStatus() == 1) {
                        homeAdapter = new HomeAdapter(retrievedMerchantList, getContext());
                        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);

                        merchantListReycler.setAdapter(homeAdapter);
                        merchantListReycler.setLayoutManager(layoutManager);

                        shimmerFrameLayout3.setVisibility(View.GONE);
                        shimmerFrameLayout3.stopShimmerAnimation();
                    } else if (homeMerchantModel.getStatus() == 0) {

                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        shimmerFrameLayout3.setVisibility(View.GONE);
                        shimmerFrameLayout3.stopShimmerAnimation();

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


                }
            });


            Call<HomeTwoModel> newCall = foodGeneeAPI.getRecomm("recommendlist", userToken, "application/x-www-form-urlencoded");
            newCall.enqueue(new Callback<HomeTwoModel>() {
                @Override
                public void onResponse(Call<HomeTwoModel> call, Response<HomeTwoModel> response) {
                    HomeTwoModel homeTwoModel = response.body();
                    List<com.foodgeene.home.hometwo.models.Merchantlist> newlist = homeTwoModel.getMerchantlist();

                    if (homeTwoModel.getStatus() == 1) {
                        homeTwoAdapter = new HomeTwoAdapter(newlist, getContext());
                        merchantTwoRecycler.setAdapter(homeTwoAdapter);
                        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
                        merchantTwoRecycler.setLayoutManager(layoutManager);

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
                }
            });


            Call<Brand> brand = foodGeneeAPI.getBrandList("bannerlist", userToken, "application/x-www-form-urlencoded");
            brand.enqueue(new Callback<Brand>() {
                @Override
                public void onResponse(Call<Brand> call, Response<Brand> response) {

                    Brand brand1 = response.body();
                    List<Bannerlist> bannerlists = brand1.getBannerlist();


                    FlipperAdapter flipper = new FlipperAdapter(getContext(), bannerlists);
                    adapterViewFlipper.setAdapter(flipper);
                    adapterViewFlipper.setFlipInterval(3000);
                    adapterViewFlipper.startFlipping();

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
            Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
            shimmerFrameLayout.setVisibility(View.GONE);
            shimmerFrameLayout.stopShimmerAnimation();
        }


    }


    private void locateAddress(Location location) throws IOException {

        try {
            Geocoder geocoder = new Geocoder(getContext());
            List<Address> addresses = null;

            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            String city = addresses.get(0).getLocality();
            String localAdd = addresses.get(0).getSubLocality();
            locationNew.setText(localAdd+","); //Ayush's map works for sublocality Geocoder API
            locationSub.setText(city);


        } catch (Exception e) {


        }


    }
}