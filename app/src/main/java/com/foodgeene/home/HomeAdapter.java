package com.foodgeene.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.foodgeene.R;
import com.foodgeene.preoder.ui.PreOrder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    List<Merchantlist> list;
    Context context;
    RequestOptions option;

    public HomeAdapter(List<Merchantlist> list, Context context) {
        this.list = list;
        this.context = context;
        option = new RequestOptions().centerCrop().placeholder(R.drawable.background).error(R.drawable.background);

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

        holder.ratingM.setText(list.get(position).getRating());

        Glide.with(context)
                .load(list.get(position).getCoverpic())
                .apply(option)
                .into(holder.merchImage);
        holder.merchName.setText(list.get(position).getStorename());
//                holder.merchType.setText(list.get(position).getStoretype());
//        holder.merchLoc.setText(list.get(position).getCity());
        if(list.get(position).getLatitude()!=null){
            if(list.get(position).getLatitude().equalsIgnoreCase("")){
                holder.iv_navigation.setVisibility(View.GONE);
            }else  holder.iv_navigation.setVisibility(View.VISIBLE);
        }else  holder.iv_navigation.setVisibility(View.GONE);

        holder.iv_navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q="+Double.valueOf(list.get(position).getLatitude())+","+Double.valueOf(list.get(position).getLongitude())+"&mode=d"));
               intent.setPackage("com.google.android.apps.maps");
                context.startActivity(intent);
            }
        });
        holder.CardViewRestraunt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(list.get(position).getShowpage().equalsIgnoreCase("1")){
                    Intent intent = new Intent(context, PreOrder.class);
                    intent.putExtra("merchantId", list.get(position).getId());
                    context.startActivity(intent);
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {

        ImageView merchImage;
        TextView ratingM;
        TextView merchName, merchType,merchLoc;
        CardView CardViewRestraunt;
        ImageView iv_navigation;
        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_navigation=itemView.findViewById(R.id.iv_navigation);
            merchImage = itemView.findViewById(R.id.wereavaialble);
            merchName = itemView.findViewById(R.id.merchantName);
//            merchType = itemView.findViewById(R.id.merchantType);
//            merchLoc = itemView.findViewById(R.id.merchantLoc);
            CardViewRestraunt = itemView.findViewById(R.id.CardViewRestraunt);
            ratingM = itemView.findViewById(R.id.ratingNo);



        }
    }
}