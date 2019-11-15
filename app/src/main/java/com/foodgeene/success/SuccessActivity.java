package com.foodgeene.success;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.foodgeene.MainActivity;
import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.register.RegisterModel;
import com.foodgeene.scanner.ScannerActivity;

import java.util.HashMap;

import network.FoodGeneeAPI;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuccessActivity extends AppCompatActivity {
    TextView Amount_text;
    Intent get;
    LinearLayout place_order_layout,detailsTaba,Loading;
    String totalamount,merchantid,productid,count,price,table,TXNID,TXNDATE;
    String UserToken;
    SessionManager sessionManager;
    LottieAnimationView animation_view;
    Integer SUCCESS_TEXT = 0;
    StringBuilder ProductIdBuilder,CountBuilder,PriceBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        Amount_text = findViewById(R.id.Amount_text);
        place_order_layout = findViewById(R.id.place_order_layout);
        detailsTaba = findViewById(R.id.detailsTaba);
        Loading = findViewById(R.id.Loading);
        animation_view = findViewById(R.id.animation_view);
        get = getIntent();
        merchantid = get.getStringExtra("merchandid");
        productid = get.getStringExtra("productid");
        count = get.getStringExtra("count");
        price = get.getStringExtra("price");
        table = get.getStringExtra("table");
        totalamount = get.getStringExtra("totalamount");
        /*ProductIdBuilder = new StringBuilder(productid);
        ProductIdBuilder.deleteCharAt(0);
        ProductIdBuilder.deleteCharAt(productid.length()-1);
        CountBuilder = new StringBuilder(count);
        CountBuilder.deleteCharAt(0);
        CountBuilder.deleteCharAt(count.length()-1);
        PriceBuilder = new StringBuilder(price);
        PriceBuilder.deleteCharAt(0);
        PriceBuilder.deleteCharAt(price.length()-1);*/
        TXNID = get.getStringExtra("TXNID");
        TXNDATE = get.getStringExtra("TXNDATE");
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        UserToken = user.get(sessionManager.USER_ID);
        Amount_text.setText("Rs. "+totalamount);
        place_order_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SuccessActivity.this, ScannerActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

            }
        });

        PlaceOrder();
    }

    public void PlaceOrder(){
        Loading.setVisibility(View.VISIBLE);
        detailsTaba.setVisibility(View.INVISIBLE);
        animation_view.setVisibility(View.INVISIBLE);

        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
        Call<RegisterModel> call = foodGeneeAPI.OrderByPrePaid("prepaid",merchantid,table,productid,count,price,totalamount,TXNID,TXNDATE,"",UserToken,"application/x-www-form-urlencoded"
        );
        call.enqueue(new Callback<RegisterModel>() {
            @Override
            public void onResponse(Call<RegisterModel> call, Response<RegisterModel> response) {
                try {
                    String status = response.body().getStatus().trim();
                    String response_text = response.body().getText().trim();
                    if(status.equals("1")){
                        Loading.setVisibility(View.INVISIBLE);
                        detailsTaba.setVisibility(View.VISIBLE);
                        animation_view.setVisibility(View.VISIBLE);
                        SUCCESS_TEXT = 1;
                    }
                    else if(status.equals("0")){
                        Toast.makeText(SuccessActivity.this, response_text, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }catch (Exception e){
                    Toast.makeText(SuccessActivity.this, "Something Went Wrong at our end.", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }

            @Override
            public void onFailure(Call<RegisterModel> call, Throwable t) {
                Toast.makeText(SuccessActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
    @Override
    public void onBackPressed() {
        if (SUCCESS_TEXT==1){
            Intent i = new Intent(SuccessActivity.this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        else {
            Toast.makeText(this, "Processing your Order! please wait....", Toast.LENGTH_SHORT).show();
        }
    }
}
