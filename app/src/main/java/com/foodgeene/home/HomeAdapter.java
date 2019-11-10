package com.foodgeene.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.foodgeene.R;

import java.util.List;

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

        Glide.with(context)
                .load(list.get(position).getCoverpic())
                .apply(option)
                .into(holder.merchImage);
                holder.merchName.setText(list.get(position).getStorename());
//                holder.merchType.setText(list.get(position).getStoretype());
//                holder.merchLoc.setText(list.get(position).getCity()+", "+list.get(position).getState());

                holder.CardViewRestraunt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, list.get(position).getStorename(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {

        ImageView merchImage;
        TextView merchName, merchType,merchLoc;
        CardView CardViewRestraunt;
        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);

            merchImage = itemView.findViewById(R.id.wereavaialble);
            merchName = itemView.findViewById(R.id.merchantName);
//            merchType = itemView.findViewById(R.id.merchantType);
//            merchLoc = itemView.findViewById(R.id.merchantLocation);
            CardViewRestraunt = itemView.findViewById(R.id.CardViewRestraunt);



        }
    }
}
