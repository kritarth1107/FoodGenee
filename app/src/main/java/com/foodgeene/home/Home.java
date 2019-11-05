package com.foodgeene.home;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.home.brandlist.BrandAdapter;
import com.foodgeene.home.brandlist.brandmodel.Brand;
import com.foodgeene.home.brandlist.brandmodel.Brandlist;
import com.foodgeene.home.hometwo.HomeTwoAdapter;
import com.foodgeene.home.hometwo.models.HomeTwoModel;

import java.util.HashMap;
import java.util.List;

import network.FoodGeneeAPI;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {

    RecyclerView merchantListReycler, merchantTwoRecycler, brandRecycler;
    HomeAdapter homeAdapter;
    ShimmerFrameLayout shimmerFrameLayout;
    HomeTwoAdapter homeTwoAdapter;
    BrandAdapter brandAdapter;


    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        merchantListReycler = rootView.findViewById(R.id.mercRecycler);
        merchantTwoRecycler = rootView.findViewById(R.id.secondRecycler);
        brandRecycler = rootView.findViewById(R.id.brandRecycler);
        //shimmer
        shimmerFrameLayout = rootView.findViewById(R.id.shimmer_view_container);

        setupRecyclerView();

        return rootView;
    }


    private void setupRecyclerView() {

        try {
            FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
            SessionManager sessionManager = new SessionManager(getContext());

            HashMap<String, String> user = sessionManager.getUserDetail();

            String userToken = user.get(sessionManager.USER_ID);

            shimmerFrameLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.startShimmerAnimation();

            Call<HomeMerchantModel> call = foodGeneeAPI.merchantList("merchants", userToken, "application/x-www-form-urlencoded");
            call.enqueue(new Callback<HomeMerchantModel>() {
                @Override
                public void onResponse(Call<HomeMerchantModel> call, Response<HomeMerchantModel> response) {


                    HomeMerchantModel homeMerchantModel = response.body();
                    List<Merchantlist> retrievedMerchantList = homeMerchantModel.getMerchantlist();

                    if (homeMerchantModel.getStatus() == 1) {
                        homeAdapter = new HomeAdapter(retrievedMerchantList, getContext());
                        merchantListReycler.setAdapter(homeAdapter);
                        merchantListReycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

                        shimmerFrameLayout.setVisibility(View.GONE);
                        shimmerFrameLayout.stopShimmerAnimation();
                    } else if (homeMerchantModel.getStatus() == 0) {

                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        shimmerFrameLayout.stopShimmerAnimation();

                    }
//                else{
//
//                    Toast.makeText(getContext(), homeMerchantModel.getStatus(), Toast.LENGTH_SHORT).show();
//                }

                }

                @Override
                public void onFailure(Call<HomeMerchantModel> call, Throwable t) {
                    Toast.makeText(getContext(), "Try again", Toast.LENGTH_SHORT).show();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    shimmerFrameLayout.stopShimmerAnimation();


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
                        merchantTwoRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));


                    } else if (homeTwoModel.getStatus() == 0) {


                    }


                }

                @Override
                public void onFailure(Call<HomeTwoModel> call, Throwable t) {

                }
            });


            Call<Brand> brand = foodGeneeAPI.getBrandList("brandlist", userToken, "application/x-www-form-urlencoded");
            brand.enqueue(new Callback<Brand>() {
                @Override
                public void onResponse(Call<Brand> call, Response<Brand> response) {

                    Brand brand1 = response.body();
                    List<Brandlist> retrievedList = brand1.getBrandlist();

                    brandAdapter = new BrandAdapter(retrievedList, getContext());
                    brandRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
                    brandRecycler.setAdapter(brandAdapter);


                }

                @Override
                public void onFailure(Call<Brand> call, Throwable t) {


                }
            });

        } catch (Exception e) {
            Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
        }


    }
}
