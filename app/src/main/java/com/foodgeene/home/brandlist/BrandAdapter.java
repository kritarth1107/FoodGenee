package com.foodgeene.home.brandlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.foodgeene.R;
import com.foodgeene.home.brandlist.brandmodel.Bannerlist;
import com.foodgeene.home.brandlist.brandmodel.Brandlist;

import java.util.List;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.BrandViewHolder>{

    List<Bannerlist> list;
    Context context;

    public BrandAdapter(List<Bannerlist> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public BrandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.brand_row, parent, false);

        BrandViewHolder holder = new BrandViewHolder(rootView);

        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BrandViewHolder holder, int position) {

        Glide.with(context)
                .load(list.get(position).getImage())
                .into(holder.brandImage);

//        holder.brandName.setText(list.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class BrandViewHolder extends RecyclerView.ViewHolder {
        TextView brandName;
        ImageView brandImage;
        public BrandViewHolder(@NonNull View itemView) {
            super(itemView);
//            brandName = itemView.findViewById(R.id.mBrandName);
            brandImage = itemView.findViewById(R.id.mBrandImage);
        }
    }
}
