package com.foodgeene.rewardsfragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.coinstransactions.CoinsTransaction;
import com.foodgeene.rewards.Rewards;
import com.foodgeene.rewards.RewardsAdapter;
import com.foodgeene.rewards.rewardmodels.RModel;
import com.foodgeene.rewards.rewardmodels.RedeemCount;
import com.foodgeene.rewards.rewardmodels.Text;
import com.foodgeene.transactionlists.CoinsTransactionsList;

import java.util.HashMap;
import java.util.List;

import network.FoodGeneeAPI;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RewardsFragment extends Fragment {

    RecyclerView recyclerView;
    RewardsAdapter rewardsAdapter;
    SessionManager sessionManager;
    ProgressBar progressBar;
    String token;
    TextView redeemCount;
    Button check;

    public RewardsFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rewards, container, false);

        recyclerView = rootView.findViewById(R.id.rewardrecycler);
        sessionManager = new SessionManager(getContext());
        HashMap<String, String> user = sessionManager.getUserDetail();
        token = user.get(sessionManager.USER_ID);
        progressBar = rootView.findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        check = rootView.findViewById(R.id.checkTransaction);
        redeemCount = rootView.findViewById(R.id.redeemCount);

        check.setOnClickListener(view -> startActivity(new Intent(getContext(), CoinsTransactionsList.class)));
        setupRecycler();
        setRedeemCount();
        return rootView;

    }

    private void setRedeemCount() {

        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
        Call<RedeemCount> countCall = foodGeneeAPI.redeemCount("rewards-count", token, "application/x-www-form-urlencoded");
        countCall.enqueue(new Callback<RedeemCount>() {
            @Override
            public void onResponse(Call<RedeemCount> call, Response<RedeemCount> response) {

                try{
                    RedeemCount retrievedCount = response.body();
                    redeemCount.setText(retrievedCount.getText());

                }
                catch (Exception e){


                }
            }

            @Override
            public void onFailure(Call<RedeemCount> call, Throwable t) {

            }
        });

    }

    private void setupRecycler() {

        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
        Call<RModel> call  = foodGeneeAPI.rewardsList("rewards-list", token,"application/x-www-form-urlencoded");

        call.enqueue(new Callback<RModel>() {
            @Override
            public void onResponse(Call<RModel> call, Response<RModel> response) {
                try{
                    RModel newModel = response.body();
                    List<Text> offers = newModel.getText();
                    rewardsAdapter = new RewardsAdapter(getContext(), offers);
                    GridLayoutManager mGridLayoutManager = new GridLayoutManager(getContext(), 2);

                    recyclerView.setLayoutManager(mGridLayoutManager);
                    recyclerView.setAdapter(rewardsAdapter);
                    progressBar.setVisibility(View.INVISIBLE);

                }
                catch (Exception e){

                    progressBar.setVisibility(View.INVISIBLE);


                }
            }

            @Override
            public void onFailure(Call<RModel> call, Throwable t) {

            }
        });
    }
}
