package com.foodgeene.cart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foodgeene.R;
import com.foodgeene.restraunt.RestrauntActivity;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrderDetails extends AppCompatActivity {
    Intent intent;
    ImageView BackImage;
    TextView rname,Order_id,TotalAmountTV,mTvServiceBoy,mTvAddMore,prepare_time,mTvCoupon;
    ProductAdapter productAdapter;
    RecyclerView recyclerView;
    LinearLayout mLLCoupon;

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
        String orderID = intent.getStringExtra("orderID");
        String couponAmount = intent.getStringExtra("couponAmount");
        if(couponAmount.equalsIgnoreCase("0"))
            mLLCoupon.setVisibility(View.GONE);
        else {
            mLLCoupon.setVisibility(View.VISIBLE);
            mTvCoupon.setText("Rs. "+couponAmount);
        }
        Log.e("encKey", encKey + "\n" + cover + "\n" + tableName);

        if (orderStatus.equalsIgnoreCase("2")){
            mTvAddMore.setVisibility(View.VISIBLE);
        prepare_time.setVisibility(View.GONE);
    }else if(orderStatus.equalsIgnoreCase("1")) {
            prepare_time.setVisibility(View.VISIBLE);
            prepare_time.setText("Prepare Time "+intent.getStringExtra("prePaidTime")+" Min");
            mTvAddMore.setVisibility(View.GONE);

        } else{ mTvAddMore.setVisibility(View.GONE);
            prepare_time.setVisibility(View.GONE);}

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



}
