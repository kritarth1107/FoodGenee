package com.foodgeene.home.hometwo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.foodgeene.R;
import com.foodgeene.home.hometwo.models.Merchantlist;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeTwoAdapter extends RecyclerView.Adapter<HomeTwoAdapter.TwoViewHolder>{

    List<Merchantlist> merchantlists;
    Context context;

    public HomeTwoAdapter(List<Merchantlist> merchantlists, Context context) {
        this.merchantlists = merchantlists;
        this.context = context;
    }

    @NonNull
    @Override
    public TwoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.home_two, parent, false);
        TwoViewHolder twoViewHolder =  new TwoViewHolder(rootView);

        return twoViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull TwoViewHolder holder, int position) {

        Glide.with(context)
                .load(merchantlists.get(position).getLogo())
                .centerCrop()
                .into(holder.mtwoImage);

//        holder.mtwoCity.setText(merchantlists.get(position).getCity());
        holder.mtwoName.setText(merchantlists.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return merchantlists.size();
    }

    public static class TwoViewHolder extends RecyclerView.ViewHolder {

        TextView mtwoName, mtwoCity;
        ImageView mtwoImage;
        public TwoViewHolder(@NonNull View itemView) {
            super(itemView);

//            mtwoCity = itemView.findViewById(R.id.mTwoCity);
            mtwoName = itemView.findViewById(R.id.mTwoName);
            mtwoImage = itemView.findViewById(R.id.mTwoImage);

        }
    }

}