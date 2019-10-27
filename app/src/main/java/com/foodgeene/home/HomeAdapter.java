package com.foodgeene.home;

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

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    List<Merchantlist> list;
    Context context;

    public HomeAdapter(List<Merchantlist> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.merchant_row, parent, false);
        HomeViewHolder homeViewHolder = new HomeViewHolder(rootView);

        return homeViewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {

        Glide.with(context)
                .load(list.get(position).getLogo())
                .into(holder.merchImage);

        holder.merchName.setText(list.get(position).getName());
        holder.merchLoc.setText(list.get(position).getCity());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {

        ImageView merchImage;
        TextView merchName, merchLoc;
        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);

            merchImage = itemView.findViewById(R.id.merchantImage);
            merchName = itemView.findViewById(R.id.merchantName);
            merchLoc = itemView.findViewById(R.id.merchLocation);



        }
    }
}
