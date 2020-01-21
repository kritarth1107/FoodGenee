package com.foodgeene.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.foodgeene.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        holder.QuantityTv.setText(" × "+products.get(position).getCount());
        holder.PriceTV.setText("Rs. "+products.get(position).getPrice());

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
