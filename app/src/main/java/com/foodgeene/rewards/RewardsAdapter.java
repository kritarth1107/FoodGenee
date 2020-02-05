package com.foodgeene.rewards;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.foodgeene.R;
import com.foodgeene.rewarddetails.RewardsDetails;
import com.foodgeene.rewards.rewardmodels.Text;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RewardsAdapter extends RecyclerView.Adapter<RewardsAdapter.RewardsViewHolder>{
    Context context;
    List<Text> list;
    String rewardId = null;

    public RewardsAdapter(Context context, List<Text> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RewardsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rewards, parent, false);
        return new RewardsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardsViewHolder holder, int position) {

        holder.rewardWon.setText(list.get(position).getTitle());
        Glide.with(context)
                .load(list.get(position).getLogo())
                .centerCrop()
                .into(holder.rewardImageHere);



        if(list.get(position).getSoldout().equalsIgnoreCase("0")){
            if(list.get(position).getValidityto().equals("0")){
                holder.expires.setText("Expired");
                holder.expires.setTextColor(Color.parseColor("#B0040D"));

            }
            else{
                holder.expires.setText("Expires on "+list.get(position).getValidityto());
                holder.expires.setTextColor(Color.parseColor("#1b7406"));
                holder.card_view.setOnClickListener(view -> {
                    rewardId = list.get(position).getId();
                    Intent intent = new Intent(context, RewardsDetails.class);
                    intent.putExtra("rewardid", rewardId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
//            Toast.makeText(context, rewardId, Toast.LENGTH_SHORT).show();
                });
            }


        }else{
            holder.expires.setText("Sold Out");
            holder.expires.setTextColor(Color.parseColor("#B0040D"));
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class RewardsViewHolder extends RecyclerView.ViewHolder {
        ImageView rewardImageHere;
        TextView expires,rewardWon;
        CardView card_view;
        public RewardsViewHolder(@NonNull View itemView) {
            super(itemView);

            rewardImageHere = itemView.findViewById(R.id.rewardImage);
            expires = itemView.findViewById(R.id.expire);
            rewardWon = itemView.findViewById(R.id.rewardWon);
            card_view=itemView.findViewById(R.id.card_view);

        }
    }
}
