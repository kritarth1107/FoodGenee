package com.foodgeene.allhotels.hotelsadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.foodgeene.R;
import com.foodgeene.allhotels.hotelsmodel.HotelsModel;
import com.foodgeene.allhotels.hotelsmodel.Merchantlist;

import java.util.List;

import retrofit2.Callback;

public class HotelsAdapter extends RecyclerView.Adapter<HotelsAdapter.HotelsViewHolder>{

    Context context;
    List<Merchantlist> list;
    OnCardClick onCardClick;

    public HotelsAdapter(Context context, List<Merchantlist> list, OnCardClick onCardClick) {
        this.context = context;
        this.list = list;
        this.onCardClick = onCardClick;
    }

    @NonNull
    @Override
    public HotelsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rest_row, parent, false);
        HotelsViewHolder hotelsViewHolder = new HotelsViewHolder(view, onCardClick);

        return hotelsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HotelsViewHolder holder, int position) {

        try{

            String rating = String.valueOf(list.get(position).getRating());
            Glide.with(context)
                    .load(list.get(position).getLogo())
                    .into(holder.restImage);

            holder.restName.setText(list.get(position).getName());
//            holder.restRating.setText(list.get(position).getRating());
            holder.restRating.setText(rating);
            holder.ratingBar.setRating(list.get(position).getRating());

        }
        catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HotelsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        ImageView restImage;
        RatingBar ratingBar;
        TextView restType;
        TextView restRating;
        TextView restName;
        OnCardClick onCardClick;

        public HotelsViewHolder(@NonNull View itemView, OnCardClick onCardClick) {
            super(itemView);

            this.onCardClick = onCardClick;
            restImage = itemView.findViewById(R.id.allRestImage);
            ratingBar = itemView.findViewById(R.id.rating_bar_indicator);
            restType = itemView.findViewById(R.id.allRestType);
            restRating = itemView.findViewById(R.id.ratingNumeric);
            restName = itemView.findViewById(R.id.allRestName);
            cardView = itemView.findViewById(R.id.cardViewClick);
            cardView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if(cardView.getId() == v.getId()){

                onCardClick.onClick(getAdapterPosition());

            }
        }
    }


    public interface OnCardClick{

        void onClick(int position);
    }
}
