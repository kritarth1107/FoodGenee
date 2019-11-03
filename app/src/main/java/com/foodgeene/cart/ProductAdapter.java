package com.foodgeene.cart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.foodgeene.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.HomeViewHolder> {

    List<Product> list;
    Context context;


    public ProductAdapter(Context context, List<Product> list) {
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.product_list_view, parent, false);
        HomeViewHolder homeViewHolder = new HomeViewHolder(rootView);

        return homeViewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {

        List<Product> products = list;

        holder.Pname.setText(products.get(position).getName());
        holder.QuantityTv.setText(products.get(position).getCount());
        holder.PriceTV.setText(products.get(position).getPrice());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView Pname,QuantityTv,PriceTV;
        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            Pname = itemView.findViewById(R.id.Pname);
            QuantityTv = itemView.findViewById(R.id.QuantityTv);
            PriceTV = itemView.findViewById(R.id.PriceTV);

        }
    }
}
