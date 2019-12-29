package com.foodgeene.redeemedlistdetails;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.redeemedlistdetails.model.RedeemedModel;
import com.foodgeene.redeemedlistdetails.model.Text;

import java.util.HashMap;
import java.util.List;

import network.FoodGeneeAPI;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RedeemedListDetails extends AppCompatActivity {
    String UserToken;
    SessionManager sessionManager;
    ImageView offerBack;
    TextView offerName, excerptO, descript, expireOn, couponCode;
    ImageView logo;
    Bundle bundle;
    String rewardId = null;
    Intent get;
    Dialog loadingDialog;
    TextView redeeemCoinsCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeemed_list_details);
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        UserToken = user.get(sessionManager.USER_ID);
        get = getIntent();
        rewardId = get.getStringExtra("rId");
        offerBack = findViewById(R.id.offerImageTwo);
        offerName = findViewById(R.id.offernameTwo);
        excerptO = findViewById(R.id.excerptTwo);
        descript = findViewById(R.id.descTwo);
        couponCode = findViewById(R.id.orignalCoupnTwo);
        expireOn = findViewById(R.id.expiresonTwo);
        logo = findViewById(R.id.restImageTwo);
//        redeeemCoinsCount = findViewById(R.id.coinsCountNew);
        drawRedeemedList(rewardId);


    }

    private void initViews() {


    }


    private void drawRedeemedList(String rewardIdHere) {

        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);

        Call<RedeemedModel> call = foodGeneeAPI.redeemedListDet("redeem-rewards-details", rewardIdHere, UserToken, "application/x-www-form-urlencoded");
        call.enqueue(new Callback<RedeemedModel>() {
            @Override
            public void onResponse(Call<RedeemedModel> call, Response<RedeemedModel> response) {
                try{
                    RedeemedModel redeemedModel = response.body();
                    Text list = redeemedModel.getText();

                    if(list.getValidityto().equals("1")){
                        expireOn.setText("Expired");
                    }
                    else{
                        expireOn.setText(list.getValidityfrom()+" - "+list.getValidityto());

                    }
                    offerName.setText(list.getTitle());
                   excerptO.setText(list.getExcerpt());
                   descript.setText(list.getDescription());
                   couponCode.setText(list.getCouponcode());
                   redeeemCoinsCount.setText(list.getCoins());


                    Glide.with(RedeemedListDetails.this)
                            .load(list.getCover())
                            .into(offerBack);

                    Glide.with(RedeemedListDetails.this)
                            .load(list.getLogo())
                            .into(logo);

                }
                catch (Exception e){

                    Toast.makeText(RedeemedListDetails.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RedeemedModel> call, Throwable t) {

            }
        });
    }
}
