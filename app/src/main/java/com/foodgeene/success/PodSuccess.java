package com.foodgeene.success;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.foodgeene.MainActivity;
import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.foodpreference.ui.AfterOrder;
import com.foodgeene.scanner.ScannerActivity;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import network.ConnectivityReceiver;
import network.FoodGeneeAPI;
import network.MyApplication;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PodSuccess extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    TextView Amount_text;
    Intent get;
    LinearLayout place_order_layout,detailsTaba,Loading;
    String totalamount,merchantid,productid,count,price,table,orderID;
    String UserToken;
    SessionManager sessionManager;
    LottieAnimationView animation_view;
    String SUCCESS_TEXT = "P";
    String orderId,couponAmount,coupon;
    Button selectPref;
    ImageView mIvBack;
    boolean isOnLine;
    String tax,tips,subscription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pod_success);
        Amount_text = findViewById(R.id.Amount_text);
        selectPref = findViewById(R.id.selectPreferences);
        place_order_layout = findViewById(R.id.place_order_layout);
        detailsTaba = findViewById(R.id.detailsTaba);
        Loading = findViewById(R.id.Loading);
        animation_view = findViewById(R.id.animation_view);
        mIvBack=findViewById(R.id.iv_back);

        isOnLine=ConnectivityReceiver.isConnected();

        get = getIntent();
        merchantid = get.getStringExtra("merchandid");
        productid = get.getStringExtra("productid");
        count = get.getStringExtra("count");
        price = get.getStringExtra("price");
        table = get.getStringExtra("table");
        totalamount = get.getStringExtra("totalamount");
        orderID=get.getStringExtra("orderID");
        couponAmount=get.getStringExtra("couponAmount");
        coupon=get.getStringExtra("coupon");
        tips=get.getStringExtra("tips");
        subscription=get.getStringExtra("subscription");
        tax=get.getStringExtra("tax");


        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        UserToken = user.get(sessionManager.USER_ID);
        Amount_text.setText("Amount Due - Rs. "+totalamount);
        place_order_layout.setOnClickListener(view -> {
            Intent i = new Intent(PodSuccess.this, ScannerActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);

        });
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PodSuccess.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
        if(isOnLine)
        PlaceOrder();
        else Toast.makeText(this, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();

        selectPref.setOnClickListener(v -> {

            Intent intent = new Intent(PodSuccess.this, AfterOrder.class);
            intent.putExtra("orderId", orderId );
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }

    public void PlaceOrder(){
        Loading.setVisibility(View.VISIBLE);
        detailsTaba.setVisibility(View.INVISIBLE);
        animation_view.setVisibility(View.INVISIBLE);
        String type="cash";
        if(orderID.equalsIgnoreCase("")) type="cash";
        else type="reordercash";


        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
        Call<PostOrderModel> call = foodGeneeAPI.OrderByCash(type,merchantid,table,productid,count,price,totalamount,orderID,couponAmount,coupon,tax,tips,subscription,UserToken,"application/x-www-form-urlencoded"
        );
        call.enqueue(new Callback<PostOrderModel>() {
            @Override
            public void onResponse(Call<PostOrderModel> call, Response<PostOrderModel> response) {
                try {
                    String status = response.body().getStatus().trim();
                    String response_text = response.body().getText().trim();
                    if(status.equals("1")){
                        Loading.setVisibility(View.GONE);
                        detailsTaba.setVisibility(View.VISIBLE);
                        animation_view.setVisibility(View.VISIBLE);
                        SUCCESS_TEXT = "S";
//                        selectPref.setVisibility(View.VISIBLE);
                        orderId = response.body().getId();
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
            public void onFailure(Call<PostOrderModel> call, Throwable t) {
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

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isOnLine=isConnected;
    }
}
