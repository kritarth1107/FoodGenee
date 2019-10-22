package com.foodgeene.restraunt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.foodgeene.R;
import com.foodgeene.scanner.Productlist;

import java.util.List;

public class RestrauntAdapter extends RecyclerView.Adapter<RestrauntAdapter.MyViewHolder> {

    private Context mContext ;
    private List<Productlist> mData ;
    RequestOptions option;
    Float quantity;


    public RestrauntAdapter(Context mContext, List<Productlist> mData) {
        this.mContext = mContext;
        this.mData = mData;
        option = new RequestOptions().centerCrop().placeholder(R.drawable.ic_image).error(R.drawable.ic_image);



    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.restraunt_product_list,parent,false) ;
        final MyViewHolder viewHolder = new MyViewHolder(view);




        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.food_title.setText(mData.get(position).getTitle());
        holder.price.setText(mData.get(position).getPrice());
        holder.sale_price.setText(mData.get(position).getSaleprice());
        holder.serve_line.setText(mData.get(position).getServeline());
        holder.tagline.setText(mData.get(position).getLabeltag());
        String av,tagline;
        av = mData.get(position).getAvailabilty();
        tagline = mData.get(position).getLabeltag();
        if(av.equals("1")){
            holder.available_layout.setVisibility(View.VISIBLE);
            holder.not_available.setVisibility(View.GONE);
        }
        else {
            holder.available_layout.setVisibility(View.GONE);
            holder.not_available.setVisibility(View.VISIBLE);
        }
        Glide.with(mContext).load(mData.get(position).getImage()).apply(option).into(holder.food_image);
        if(tagline.equals("")){
            holder.rest_tagline_logo.setVisibility(View.GONE);
            holder.tagline.setVisibility(View.GONE);
        }
        else{
            holder.rest_tagline_logo.setVisibility(View.VISIBLE);
            holder.tagline.setVisibility(View.VISIBLE);
        }

        if(holder.quantity_tv.getText().equals("0")){
            holder.quantity_card.setVisibility(View.GONE);
            holder.subtract.setVisibility(View.GONE);
        }
        String Q_String = holder.quantity_tv.getText().toString().trim();
        quantity = Float.parseFloat(Q_String);



    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {


        TextView sale_price,price,tagline,serve_line,quantity_tv,not_available,food_title;
        ImageView food_image,rest_tagline_logo;
        LinearLayout available_layout;
        CardView add,subtract,quantity_card;



        public MyViewHolder(View itemView) {
            super(itemView);

            sale_price = itemView.findViewById(R.id.sale_price_tv);
            price = itemView.findViewById(R.id.price_tv);
            quantity_tv = itemView.findViewById(R.id.quantity_tv);
            not_available = itemView.findViewById(R.id.not_available_tv);
            available_layout = itemView.findViewById(R.id.available_layout);
            food_title = itemView.findViewById(R.id.food_title_tv);
            serve_line = itemView.findViewById(R.id.serve_line_tv);
            tagline = itemView.findViewById(R.id.lable_tag_tv);
            food_image = itemView.findViewById(R.id.food_image);
            rest_tagline_logo = itemView.findViewById(R.id.rest_tagline_logo);
            add = itemView.findViewById(R.id.add_button);
            subtract = itemView.findViewById(R.id.subtract_button);
            quantity_card = itemView.findViewById(R.id.quantity_card);




        }
    }

}



