package com.foodgeene.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.Serializable;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foodgeene.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderDetails extends AppCompatActivity {
    Intent intent;
    ImageView BackImage;
    TextView rname,Order_id,TotalAmountTV;
    ProductAdapter productAdapter;
    RecyclerView recyclerView;
    LinearLayout Pending,Success,Failed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        Order_id = findViewById(R.id.Order_id);
        rname = findViewById(R.id.restName);
        BackImage = findViewById(R.id.BackImage);
        recyclerView = findViewById(R.id.recyclerViewDetails);
        TotalAmountTV = findViewById(R.id.TotalAmountTV);
        BackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        intent = getIntent();
        String restName = intent.getStringExtra("Restraunt");
        String OdId = intent.getStringExtra("id");
        String paymentMethod =intent.getStringExtra("paymentMethod");;
        String orderStatus =intent.getStringExtra("orderStatus");;
        String paymentStatus =intent.getStringExtra("paymentStatus");;
        String totalAmount =intent.getStringExtra("totalAmount");;
        String tableName=intent.getStringExtra("tableName");;

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
