package com.foodgeene.cart;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.preoder.preordermodel.PreOrderModel;
import com.foodgeene.restraunt.RestrauntActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import network.FoodGeneeAPI;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetails extends AppCompatActivity {
    Intent intent;
    ImageView BackImage,im_verify;
    TextView rname,Order_id,TotalAmountTV,mTvServiceBoy,mTvAddMore,prepare_time,mTvCoupon,mTvInvoice,mTvTax,mTvTip,mTvSubscription;
    ProductAdapter productAdapter;
    RecyclerView recyclerView;
    LinearLayout mLLCoupon;
    String orderID,showaddmore;
    LinearLayout mLLTax,mLLTip,mLLSubscription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        Order_id = findViewById(R.id.Order_id);
        rname = findViewById(R.id.restName);
        mTvAddMore = findViewById(R.id.tv_add_more);
        BackImage = findViewById(R.id.BackImage);
        recyclerView = findViewById(R.id.recyclerViewDetails);
        TotalAmountTV = findViewById(R.id.TotalAmountTV);
        mTvServiceBoy = findViewById(R.id.tv_service_boy);
        prepare_time = findViewById(R.id.prepare_time);
        mLLCoupon=findViewById(R.id.ll_coupon);
        mTvCoupon=findViewById(R.id.tv_coupon);
        im_verify=findViewById(R.id.im_verify);
        mTvInvoice=findViewById(R.id.tv_invoice);
        mTvTax=findViewById(R.id.tv_tax);
        mTvTip=findViewById(R.id.tv_tip);
        mTvSubscription=findViewById(R.id.tv_subscription);
        mLLSubscription=findViewById(R.id.ll_subscription);
        mLLTax=findViewById(R.id.ll_tax);
        mLLTip=findViewById(R.id.ll_tip);

        BackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        intent = getIntent();
        String restName = intent.getStringExtra("Restraunt");
        String OdId = intent.getStringExtra("id");
        String paymentMethod = intent.getStringExtra("paymentMethod");
        String orderStatus = intent.getStringExtra("orderStatus");
        String paymentStatus = intent.getStringExtra("paymentStatus");
        String totalAmount = intent.getStringExtra("totalAmount");
        String tableName = intent.getStringExtra("tableName");
        String encKey = intent.getStringExtra("encKey");
        String cover = intent.getStringExtra("cover");
        String serviceBoy = intent.getStringExtra("serviceBoy");
         orderID = intent.getStringExtra("orderID");
        showaddmore=intent.getStringExtra("showaddmore");
        String couponAmount = intent.getStringExtra("couponAmount");
        String verify=intent.getStringExtra("verify");
        if(intent.getStringExtra("tip").equalsIgnoreCase("0")||intent.getStringExtra("tip").equalsIgnoreCase("")){
            mLLTip.setVisibility(View.GONE);
        }else{
            mLLTip.setVisibility(View.VISIBLE);
            mTvTip.setText("Rs. "+intent.getStringExtra("tip"));
        }

        if(intent.getStringExtra("tax").equalsIgnoreCase("0")||intent.getStringExtra("tax").equalsIgnoreCase("")){
            mLLTax.setVisibility(View.GONE);
        }else{
            mLLTax.setVisibility(View.VISIBLE);
            mTvTax.setText("Rs. "+intent.getStringExtra("tax"));
        }

        if(intent.getStringExtra("subscription").equalsIgnoreCase("0")||intent.getStringExtra("subscription").equalsIgnoreCase("")){
            mLLSubscription.setVisibility(View.GONE);
        }else{
            mLLSubscription.setVisibility(View.VISIBLE);
            mTvSubscription.setText("Rs. "+intent.getStringExtra("subscription"));
        }

        if(verify.equalsIgnoreCase("0")){
            im_verify.setVisibility(View.GONE);
        }else im_verify.setVisibility(View.VISIBLE);
        if(couponAmount.equalsIgnoreCase("0"))
            mLLCoupon.setVisibility(View.GONE);
        else {
            mLLCoupon.setVisibility(View.VISIBLE);
            mTvCoupon.setText("Rs. "+couponAmount);
        }
        Log.e("encKey", encKey + "\n" + cover + "\n" + tableName);
        if(showaddmore.equalsIgnoreCase("1"))
            mTvAddMore.setVisibility(View.VISIBLE);
        else  mTvAddMore.setVisibility(View.GONE);

        if (orderStatus.equalsIgnoreCase("2")){
        prepare_time.setVisibility(View.GONE);
    }else if(orderStatus.equalsIgnoreCase("1")) {
            prepare_time.setVisibility(View.VISIBLE);
           // prepare_time.setText("Prepare Time "+intent.getStringExtra("prePaidTime")+" Min");


            if(intent.getStringExtra("prePaidTime").equalsIgnoreCase("0")){
                prepare_time.setText("done!") ;
            }else{
                Log.e("timer",intent.getStringExtra("prePaidTime"));
                Log.e("milliseconds",""+TimeUnit.SECONDS.toMillis(Integer.valueOf(intent.getStringExtra("prePaidTime"))));
                new CountDownTimer(TimeUnit.SECONDS.toMillis(Integer.valueOf(intent.getStringExtra("prePaidTime"))), 1000) {

                    public void onTick(long millisUntilFinished) {
                        String text = String.format(Locale.getDefault(), "%02d min: %02d sec",
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                        prepare_time.setText("Prepare Time : " + text);
                    }

                    public void onFinish() {
                        prepare_time.setText("done!");
                    }
                }.start();
            }


        } else{
            prepare_time.setVisibility(View.GONE);}

        if(orderStatus.equalsIgnoreCase("2")||orderStatus.equalsIgnoreCase("4"))
            mTvInvoice.setVisibility(View.VISIBLE);
        else  mTvInvoice.setVisibility(View.GONE);

        mTvInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(OrderDetails.this);
                dialog.setContentView(R.layout.lay_invoice);
                //dialog.setCancelable(false);
                EditText text = dialog.findViewById(R.id.et_email);
                Button submit = dialog.findViewById(R.id.b_submit);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(text.getText().toString().length()>0&&text.getText().toString()!=null&&text.getText().toString()!=""){

                            if(isValidEmailId(text.getText().toString())){
                                SessionManager sessionManager = new SessionManager(OrderDetails.this);

                                HashMap<String, String> user = sessionManager.getUserDetail();

                                String userToken = user.get(sessionManager.USER_ID);

                                FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
                                Call<PreOrderModel> call = foodGeneeAPI.getInvoice("orderinvoice", orderID,text.getText().toString(), userToken, "application/x-www-form-urlencoded");
                                call.enqueue(new Callback<PreOrderModel>() {
                                    @Override
                                    public void onResponse(Call<PreOrderModel> call, Response<PreOrderModel> response) {


                                        assert response.body() != null;
                                        if(response.body().getStatus().equals(1)) {
                                            dialog.dismiss();
                                            Toast.makeText(OrderDetails.this, "Sent Successfully", Toast.LENGTH_SHORT).show();

                                        } else if(response.body().getStatus().equals(0)){
                                            dialog.dismiss();
                                            Toast.makeText(OrderDetails.this, "Unable to send", Toast.LENGTH_SHORT).show();
                                        } else {
                                            dialog.dismiss();
                                            Toast.makeText(OrderDetails.this, "Error", Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<PreOrderModel> call, Throwable t) {

                                    }
                                });
                            }else Toast.makeText(OrderDetails.this, "Invalid email id", Toast.LENGTH_SHORT).show();


                        }

                    }
                });

                dialog.show();
                dialog.show();
            }
        });

        mTvAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderDetails.this, RestrauntActivity.class);
                intent.putExtra("encKey",encKey);
                intent.putExtra("store",restName);
                intent.putExtra("table",tableName);
                intent.putExtra("cover",cover);
                intent.putExtra("from","order");
                intent.putExtra("orderId",orderID);
                intent.putExtra("verify",verify);
                intent.putExtra("servingtype","Restaurant");
                startActivity(intent);

            }
        });

        if(intent.getStringExtra("serviceBoy")!=null)
            mTvServiceBoy.setText("Served By : "+serviceBoy);

        rname.setText(restName);
        Order_id.setText(OdId);
        TotalAmountTV.setText("Rs. "+totalAmount);
        try{

            Bundle args = intent.getBundleExtra("BUNDLE");
            List<Product> products = (List<Product>) args.getSerializable("ARRAYLIST");
            productAdapter = new ProductAdapter(this,products);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(productAdapter);
        }catch (Exception e){
            Toast.makeText(this, "No Product Found", Toast.LENGTH_SHORT).show();
        }

    }
    private boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }


}
