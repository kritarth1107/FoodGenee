package com.foodgeene.success;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.foodgeene.register.RegistrationActivity;
import com.foodgeene.restraunt.RestrauntActivity;
import com.foodgeene.restraunt.RestrauntAdapter;
import com.foodgeene.scanner.Productlist;
import com.foodgeene.scanner.ScannerActivity;
import com.foodgeene.scanner.ScannerModel;

import java.util.HashMap;
import java.util.List;

import network.FoodGeneeAPI;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PodSuccess extends AppCompatActivity {
    TextView Amount_text;
    Intent get;
    LinearLayout place_order_layout,detailsTaba,Loading;
    String totalamount,merchantid,productid,count,price,table;
    String UserToken;
    SessionManager sessionManager;
    LottieAnimationView animation_view;
    String SUCCESS_TEXT = "P";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pod_success);
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
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        UserToken = user.get(sessionManager.USER_ID);
        Amount_text.setText("Amount Due - Rs. "+totalamount);
        place_order_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PodSuccess.this, MainActivity.class);
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
        Call<RegisterModel> call = foodGeneeAPI.OrderByCash("cash",merchantid,table,productid,count,price,totalamount,UserToken,"application/x-www-form-urlencoded"
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
                        SUCCESS_TEXT = "S";
                    }
                    else if(status.equals("0")){
                        Toast.makeText(PodSuccess.this, response_text, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }catch (Exception e){
                    Toast.makeText(PodSuccess.this, "Something Went Wrong at our end.", Toast.LENGTH_SHORT).show();
                    finish();
                }





            }

            @Override
            public void onFailure(Call<RegisterModel> call, Throwable t) {
                Toast.makeText(PodSuccess.this, t.toString(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (SUCCESS_TEXT.equals("S")){
            Intent i = new Intent(PodSuccess.this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        else {
            Toast.makeText(this, "Processing your Order! please wait....", Toast.LENGTH_SHORT).show();
        }
    }
}
