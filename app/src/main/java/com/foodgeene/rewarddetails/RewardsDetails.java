package com.foodgeene.rewarddetails;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
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
import com.foodgeene.rewarddetails.model.DetailModel;
import com.foodgeene.rewarddetails.model.RedeemCoinsModel;
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
    Dialog loadingDialog;



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

        redeem.setOnClickListener(view -> {
            loadingDialog = new Dialog(RewardsDetails.this);
            loadingDialog.setContentView(R.layout.loading_dialog_layout);
            loadingDialog.show();
            loadingDialog.setCancelable(false);
            loadingDialog.setCanceledOnTouchOutside(false);

            FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
            Call<RedeemCoinsModel> call = foodGeneeAPI.redeemCoins("redeem-coins", rewardId, UserToken, "application/x-www-form-urlencoded");
            call.enqueue(new Callback<RedeemCoinsModel>() {
                @Override
                public void onResponse(Call<RedeemCoinsModel> call, Response<RedeemCoinsModel> response) {

                        RedeemCoinsModel redeemCoins = response.body();

                        if(redeemCoins.getStatusO().equals("1")){
                            couponCode.setText(redeemCoins.getCouponcode0());
                            hideCoupon.setVisibility(View.VISIBLE);
                            redeem.setVisibility(View.GONE);
                            Toast.makeText(RewardsDetails.this, redeemCoins.getMessage0(), Toast.LENGTH_SHORT).show();
                            loadingDialog.cancel();
                            loadingDialog.dismiss();

                        }
                        else if(redeemCoins.getStatusO().equals("0")){
                            loadingDialog.cancel();
                            loadingDialog.dismiss();
                            Toast.makeText(RewardsDetails.this, redeemCoins.getMessage0(), Toast.LENGTH_SHORT).show();

                        }

                        else {
                            loadingDialog.cancel();
                            loadingDialog.dismiss();

                        }
                    }






                @Override
                public void onFailure(Call<RedeemCoinsModel> call, Throwable t) {
                    loadingDialog.cancel();
                    loadingDialog.dismiss();
                }
            });

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
                    if(detailModel.getText().getValidityto().equals("1")){
                        expireOn.setText("Expired");
                        redeem.setVisibility(View.GONE);
                    }
                    else {
                        expireOn.setText(text.getValidityfrom()+" - "+text.getValidityto());

                    }
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
                catch (Exception ignored){

                    loadingDialog.cancel();
                    loadingDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<DetailModel> call, Throwable t) {
                loadingDialog.cancel();
                loadingDialog.dismiss();
            }
        });

    }
}
