package com.foodgeene.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foodgeene.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;

public class AdapterRe extends RecyclerView.Adapter<AdapterRe.MyViewHolder> {
 
    private LayoutInflater inflater;
    List<HomeMerchantModel> location;
    List<HomeMerchantModel> searchLocation;
    private RecyclerItemClickListener recyclerItemClickListener;
 
    public AdapterRe(Context ctx, List<HomeMerchantModel> location){
 
        inflater = LayoutInflater.from(ctx);
        this.location = location;
        this.searchLocation=new ArrayList<>();
        this.searchLocation.addAll(location);
    }
 
    @Override
    public AdapterRe.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
 
        View view = inflater.inflate(R.layout.rv_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
 
        return holder;
    }
    public void setOnCheckedListener(RecyclerItemClickListener recyclerItemClickListener) {
        this.recyclerItemClickListener = recyclerItemClickListener;
    }
 
    @Override
    public void onBindViewHolder(AdapterRe.MyViewHolder holder, int position) {
 
        holder.name.setText(location.get(position).getLocation());
        holder.city.setText(location.get(position).getCity());
        holder.mLLLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerItemClickListener.onItemClickListener(position);
            }
        });
 
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        location.clear();
        if (charText.length() == 0) {
            location.addAll(searchLocation);
        } else {
            for (HomeMerchantModel wp : searchLocation) {
                if (wp.getLocation().toLowerCase(Locale.getDefault()).contains(charText)) {
                    location.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return location.size();
    }
 
    class MyViewHolder extends RecyclerView.ViewHolder{
 
        TextView name,city;
        LinearLayout mLLLocation;
 
 
        public MyViewHolder(View itemView) {
            super(itemView);
 
            name =  itemView.findViewById(R.id.name);
            city =  itemView.findViewById(R.id.city);
            mLLLocation=itemView.findViewById(R.id.ll_location);
 

 
        }
 
    }
}