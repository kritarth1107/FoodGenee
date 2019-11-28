package com.foodgeene.coinstransactions.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.foodgeene.R;
import com.foodgeene.coinstransactions.model.Text;
import com.foodgeene.redeemedlistdetails.RedeemedListDetails;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    Context context;
    List<Text> list;

    public TransactionAdapter(Context context, List<Text> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.coinstransaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {

//        holder.reasonHere.setText(list.get(position).getReason());
        Glide.with(context)
                .load(list.get(position).getLogo())
                .into(holder.tranLogo);

        holder.transName.setText(list.get(position).getTitle());
        holder.transExcerpt.setText(list.get(position).getExcerpt());
        if(list.get(position).getValidityto().equals("1")){
            holder.transValid.setText("Reward Expired");

        }
        else{

            holder.transValid.setText("Validity "+list.get(position).getValidityto());

        }

        holder.touch.setOnClickListener(view -> {
            String id = list.get(position).getId();
            Intent intent = new Intent(context, RedeemedListDetails.class);
            intent.putExtra("rId", id);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView transName, transValid, transExcerpt;
        ImageView tranLogo;
        CardView touch;
//        TextView reasonHere;
        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);

            tranLogo = itemView.findViewById(R.id.tranLogo);
            transName = itemView.findViewById(R.id.tranTitle);
            transExcerpt = itemView.findViewById(R.id.tranDesc);
            transValid = itemView.findViewById(R.id.tranValidity);
            touch = itemView.findViewById(R.id.touchCard);
//            reasonHere = itemView.findViewById(R.id.reason);
        }
    }
}
