package com.foodgeene.rewards;

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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.foodgeene.R;
import com.foodgeene.rewarddetails.RewardsDetails;
import com.foodgeene.rewards.rewardmodels.Text;

import java.util.List;

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
                .into(holder.rewardImageHere);

        holder.rewardImageHere.setOnClickListener(view -> {
            rewardId = list.get(position).getId();
            Intent intent = new Intent(context, RewardsDetails.class);
            intent.putExtra("rewardid", rewardId);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
//            Toast.makeText(context, rewardId, Toast.LENGTH_SHORT).show();
        });

        if(list.get(position).getValidityto().equals("1")){
            holder.expires.setText("Expired");

        }
        else{
            holder.expires.setText("Expires on "+list.get(position).getValidityto());

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class RewardsViewHolder extends RecyclerView.ViewHolder {
        ImageView rewardImageHere;
        TextView expires,rewardWon;
        public RewardsViewHolder(@NonNull View itemView) {
            super(itemView);

            rewardImageHere = itemView.findViewById(R.id.rewardImage);
            expires = itemView.findViewById(R.id.expire);
            rewardWon = itemView.findViewById(R.id.rewardWon);

        }
    }
}
