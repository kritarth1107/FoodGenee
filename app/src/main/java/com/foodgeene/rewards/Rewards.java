package com.foodgeene.rewards;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.coinstransactions.CoinsTransaction;
import com.foodgeene.rewards.rewardmodels.RModel;
import com.foodgeene.rewards.rewardmodels.RedeemCount;
import com.foodgeene.rewards.rewardmodels.Text;

import java.util.HashMap;
import java.util.List;

import network.FoodGeneeAPI;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Rewards extends AppCompatActivity {

    RecyclerView recyclerView;
    RewardsAdapter rewardsAdapter;
    SessionManager sessionManager;
    ProgressBar progressBar;
    String token;
    TextView redeemCount;
    Button check;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);
        recyclerView = findViewById(R.id.rewardrecycler);
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        token = user.get(sessionManager.USER_ID);
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        check = findViewById(R.id.checkTransaction);
        redeemCount = findViewById(R.id.redeemCount);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Rewards.this, CoinsTransaction.class));
            }
        });
        setupRecycler();
        setRedeemCount();
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
                    rewardsAdapter = new RewardsAdapter(Rewards.this, offers);
                    GridLayoutManager mGridLayoutManager = new GridLayoutManager(Rewards.this, 2);

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
