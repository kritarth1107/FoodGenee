package com.foodgeene.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.foodgeene.R;
import com.foodgeene.profile.userdetails.Users;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CoinAdapter extends  RecyclerView.Adapter<CoinAdapter.NewViewHolder>{

    Context context;
    List<Users> list;

    public CoinAdapter(Context context, List<Users> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public NewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_coin, parent, false);
        NewViewHolder newViewHolder = new NewViewHolder(view);
        return newViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewViewHolder holder, int position) {


        try {

            Glide.with(context)
                    .load(list.get(position).getImage())
                    .into(holder.propic);
            holder.tv_coin_user.setText("Name : "+list.get(position).getName());
            holder.tv_coins.setText(list.get(position).getCoins());

            if(position==0){
                holder.mLLBackground.setBackgroundResource(R.drawable.gold);
            }else  holder.mLLBackground.setBackgroundResource(0);



        }
        catch (Exception e){



        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NewViewHolder extends RecyclerView.ViewHolder  {
        TextView tv_coin_user, tv_coins;
        ImageView propic;
        LinearLayout mLLBackground;

        public NewViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_coin_user = itemView.findViewById(R.id.tv_coin_user);
            tv_coins = itemView.findViewById(R.id.tv_coins);
            propic=itemView.findViewById(R.id.propic);
            mLLBackground=itemView.findViewById(R.id.ll_background)  ;

        }


    }



}
