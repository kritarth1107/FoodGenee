package com.foodgeene.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.foodgeene.R;

import java.util.ArrayList;
import java.util.List;

public class OrderlistAdapter extends RecyclerView.Adapter<OrderlistAdapter.HomeViewHolder> {

    List<Product> list;
    Context context;
    RequestOptions option;


    public OrderlistAdapter(Context context,List<Product> list) {
        this.list = list;
        this.context = context;
        option = new RequestOptions().centerCrop().placeholder(R.drawable.background).error(R.drawable.background);

    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.order_row, parent, false);
        HomeViewHolder homeViewHolder = new HomeViewHolder(rootView);

        return homeViewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {

        holder.Amount_cart.setText(list.get(position).getPrice());
        holder.Name_cart.setText(list.get(position).getName());
        holder.Quantity_cart.setText(list.get(position).getCount());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView Name_cart,Quantity_cart,Amount_cart;
        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            Name_cart = itemView.findViewById(R.id.Name_cart);
            Quantity_cart = itemView.findViewById(R.id.Quantity_cart);
            Amount_cart = itemView.findViewById(R.id.Amount_Cart);




        }
    }
}
