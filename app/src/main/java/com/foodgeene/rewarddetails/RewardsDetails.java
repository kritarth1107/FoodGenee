package com.foodgeene.rewarddetails;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.profile.userdetails.UserModel;
import com.foodgeene.rewarddetails.model.DetailModel;
import com.foodgeene.rewarddetails.model.RedeemCoins;
import com.foodgeene.rewarddetails.model.Text;

import java.util.HashMap;

import network.FoodGeneeAPI;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RewardsDetails extends AppCompatActivity {

    ImageView offerBack;
    TextView offerName, excerptO, descript, expireOn, couponCode;
    ImageView logo;
    Intent get;
    String rewardId = null;
    String UserToken;
    SessionManager sessionManager;
    ProgressBar progressBar;
    RelativeLayout hideLay;
    RelativeLayout hideCoupon;

    Button redeem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards_details);
        progressBar = findViewById(R.id.progressDetail);
        hideCoupon = findViewById(R.id.hideCoupon);
        offerBack = findViewById(R.id.offerImage);
        sessionManager = new SessionManager(this);
        offerName = findViewById(R.id.offername);
        excerptO = findViewById(R.id.excerpt);
        descript = findViewById(R.id.desc);
        couponCode = findViewById(R.id.orignalCoupn);
        expireOn = findViewById(R.id.expireson);
        hideLay  = findViewById(R.id.hideLay);
        logo = findViewById(R.id.restImage);
        redeem = findViewById(R.id.orignalRedeemButton);
        HashMap<String, String> user = sessionManager.getUserDetail();
        UserToken = user.get(sessionManager.USER_ID);

        hideLay.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        get = getIntent();
        rewardId = get.getStringExtra("rewardid");

        redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
                Call<RedeemCoins> call = foodGeneeAPI.redeemCoins("redeem-coins", rewardId, UserToken, "application/x-www-form-urlencoded");
                call.enqueue(new Callback<RedeemCoins>() {
                    @Override
                    public void onResponse(Call<RedeemCoins> call, Response<RedeemCoins> response) {
                        try{
                            RedeemCoins redeemCoins = response.body();
                            couponCode.setText(redeemCoins.getCouponcode());
                            hideCoupon.setVisibility(View.VISIBLE);
                            redeem.setVisibility(View.GONE);
                            Toast.makeText(RewardsDetails.this, redeemCoins.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                        catch (Exception e){


                        }

                    }

                    @Override
                    public void onFailure(Call<RedeemCoins> call, Throwable t) {

                    }
                });

            }
        });

        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
        Call<DetailModel> call =  foodGeneeAPI.rewardListDetails("rewards-details", rewardId, UserToken, "application/x-www-form-urlencoded");
        call.enqueue(new Callback<DetailModel>() {
            @Override
            public void onResponse(Call<DetailModel> call, Response<DetailModel> response) {

                try{

                    DetailModel detailModel = response.body();
                    Text text = detailModel.getText();

                    offerName.setText(text.getTitle());
                    excerptO.setText(text.getExcerpt());
                    expireOn.setText("Validity "+text.getValidityfrom()+" to "+text.getValidityto());
                    descript.setText(text.getDescription());
                    hideLay.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    Glide.with(getApplicationContext())
                            .load(text.getLogo())
                            .into(logo);

                    Glide.with(getApplicationContext())
                            .load(text.getCover())
                            .into(offerBack);



                }
                catch (Exception e){


                }

            }

            @Override
            public void onFailure(Call<DetailModel> call, Throwable t) {

            }
        });

    }
}
