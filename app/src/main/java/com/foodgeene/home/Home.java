package com.foodgeene.home;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;

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

    RecyclerView merchantListReycler;
    HomeAdapter homeAdapter;


    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        merchantListReycler = rootView.findViewById(R.id.mercRecycler);

        bindViews(rootView);
        setupRecyclerView();

        return rootView;
    }

    private void bindViews(View rootView) {

    }

    private void setupRecyclerView() {

        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
        SessionManager sessionManager = new SessionManager(getContext());

        HashMap<String, String> user = sessionManager.getUserDetail();

        String userToken = user.get(sessionManager.USER_ID);


        Call<HomeMerchantModel> call = foodGeneeAPI.merchantList("merchants", userToken, "application/x-www-form-urlencoded");
        call.enqueue(new Callback<HomeMerchantModel>() {
            @Override
            public void onResponse(Call<HomeMerchantModel> call, Response<HomeMerchantModel> response) {

                HomeMerchantModel  homeMerchantModel = response.body();
                List<Merchantlist> retrievedMerchantList = homeMerchantModel.getMerchantlist();

                if(homeMerchantModel.getStatus()==1){
                    homeAdapter = new HomeAdapter(retrievedMerchantList, getContext());
                    merchantListReycler.setAdapter(homeAdapter);
                    merchantListReycler.setLayoutManager(new LinearLayoutManager(getContext()));
                }
                else if(homeMerchantModel.getStatus()==0){

                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

                }
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

    }

}
