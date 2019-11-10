package com.foodgeene.home;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
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
import com.foodgeene.home.brandlist.brandmodel.Bannerlist;
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
    ShimmerFrameLayout shimmerFrameLayout,shimmerFrameLayout2,shimmerFrameLayout3;
    HomeTwoAdapter homeTwoAdapter;
    BrandAdapter brandAdapter;
    LinearLayoutManager layoutManager;


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
        shimmerFrameLayout2 = rootView.findViewById(R.id.shimmer_view_container2);
        shimmerFrameLayout3 = rootView.findViewById(R.id.shimmer_view_container3);

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

                    brandAdapter = new BrandAdapter(bannerlists, getContext());
                    layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
                    brandRecycler.setLayoutManager(layoutManager);
                    brandRecycler.setAdapter(brandAdapter);
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(brandRecycler.getContext(),
                                layoutManager.getOrientation());
                        brandRecycler.addItemDecoration(dividerItemDecoration);


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
}
