package com.foodgeene.restraunt;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.foodgeene.R;
import com.foodgeene.payment.PaymentMethod;
import com.foodgeene.scanner.Productlist;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.List;

public class RestrauntAdapter extends RecyclerView.Adapter<RestrauntAdapter.MyViewHolder> {

    private Context mContext ;
    private List<Productlist> mData ;
    private  LinearLayout OrderSheet,ViewCartLayout;
    private TextView Total_amount,Quanity_item_tv;
    RequestOptions option;
    Integer quantity,finalAmmount=0,total_quantity=0;
    String Q_String;
    ArrayList<String> itemIdList = new ArrayList<String>();
    ArrayList<String> itemCountList = new ArrayList<String>();
    ArrayList<String> itemPriceList = new ArrayList<String>();
    String merchandid,table;



    BottomSheetBehavior bottomSheetBehavior;

    public RestrauntAdapter(Context mContext, List<Productlist> mData,LinearLayout OrderSheet,TextView Total_amount,TextView Quanity_item_tv,LinearLayout ViewCartLayout,String table, String merchandid) {
        this.mContext = mContext;
        this.mData = mData;
        this.OrderSheet = OrderSheet;
        this.Total_amount = Total_amount;
        this.Quanity_item_tv = Quanity_item_tv;
        this.merchandid = merchandid;
        this.table = table;
        this.ViewCartLayout = ViewCartLayout;
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
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

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
            holder.add.setVisibility(View.GONE);
            holder.subtract.setVisibility(View.GONE);
            holder.quantity_card.setVisibility(View.GONE);
            holder.add_now_tv.setVisibility(View.VISIBLE);
        }
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Q_String = holder.quantity_tv.getText().toString().trim();
                quantity = Integer.parseInt(Q_String);
                quantity = quantity+1;
                holder.quantity_tv.setText(String.valueOf(quantity));
                holder.quantity_tv.setAnimation(AnimationUtils.loadAnimation(mContext,android.R.anim.slide_in_left));
                String saleP = mData.get(position).getSaleprice();
                Integer SalePrice = Integer.parseInt(saleP);
                finalAmmount = finalAmmount+SalePrice;
                Total_amount.setText(String.valueOf(finalAmmount));
                total_quantity = total_quantity+1;
                Integer price = quantity*SalePrice;
                Quanity_item_tv.setText(String.valueOf(total_quantity));
                int pos = itemIdList.indexOf(mData.get(position).getId());
                itemIdList.remove(pos);
                itemCountList.remove(pos);
                itemPriceList.remove(pos);
                itemIdList.add(mData.get(position).getId());
                itemCountList.add(String.valueOf(quantity));
                itemPriceList.add(String.valueOf(price));


            }
        });

        holder.subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Q_String = holder.quantity_tv.getText().toString().trim();
                quantity = Integer.parseInt(Q_String);
                quantity = quantity-1;

                String saleP = mData.get(position).getSaleprice();
                Integer SalePrice = Integer.parseInt(saleP);
                finalAmmount = finalAmmount-SalePrice;
                if(finalAmmount==0){
                    OrderSheet.setVisibility(View.GONE);
                    OrderSheet.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.slide_down));
                }
                Total_amount.setText(String.valueOf(finalAmmount));
                total_quantity = total_quantity-1;
                Quanity_item_tv.setText(String.valueOf(total_quantity));
                Integer price = quantity*SalePrice;

                if(quantity>0){

                    holder.quantity_tv.setText(String.valueOf(quantity));
                    holder.quantity_tv.setAnimation(AnimationUtils.loadAnimation(mContext,android.R.anim.slide_in_left));
                    int pos = itemIdList.indexOf(mData.get(position).getId());
                    itemIdList.remove(pos);
                    itemCountList.remove(pos);
                    itemPriceList.remove(pos);
                    itemIdList.add(mData.get(position).getId());
                    itemCountList.add(String.valueOf(quantity));
                    itemPriceList.add(String.valueOf(price));

                }
                else{
                    holder.add.setVisibility(View.GONE);
                    holder.subtract.setVisibility(View.GONE);
                    holder.quantity_card.setVisibility(View.GONE);
                    holder.add_now_tv.setVisibility(View.VISIBLE);
                    holder.quantity_tv.setText("0");
                    holder.add_now_tv.setAnimation(AnimationUtils.loadAnimation(mContext,android.R.anim.slide_in_left));
                    holder.add.setAnimation(AnimationUtils.loadAnimation(mContext,android.R.anim.slide_out_right));
                    holder.quantity_card.setAnimation(AnimationUtils.loadAnimation(mContext,android.R.anim.slide_out_right));
                    holder.subtract.setAnimation(AnimationUtils.loadAnimation(mContext,android.R.anim.slide_out_right));
                    int pos = itemIdList.indexOf(mData.get(position).getId());
                    itemIdList.remove(pos);
                    itemCountList.remove(pos);
                    itemPriceList.remove(pos);
                }
            }
        });

        holder.add_now_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.add_now_tv.setVisibility(View.GONE);
                holder.add.setVisibility(View.VISIBLE);
                holder.subtract.setVisibility(View.VISIBLE);
                holder.quantity_card.setVisibility(View.VISIBLE);
                holder.quantity_tv.setText("1");
                holder.quantity_tv.setAnimation(AnimationUtils.loadAnimation(mContext,android.R.anim.slide_in_left));
                holder.add_now_tv.setAnimation(AnimationUtils.loadAnimation(mContext,android.R.anim.slide_out_right));

                if(finalAmmount==0){
                    OrderSheet.setVisibility(View.VISIBLE);
                    OrderSheet.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.slide_up));
                }
                String saleP = mData.get(position).getSaleprice();
                Integer SalePrice = Integer.parseInt(saleP);
                finalAmmount = finalAmmount+SalePrice;
                Total_amount.setText(String.valueOf(finalAmmount));
                total_quantity = total_quantity+1;
                Quanity_item_tv.setText(String.valueOf(total_quantity));
                String Item_Name =mData.get(position).getTitle();
                String Item_Sale_Price =mData.get(position).getSaleprice();
                String Item_Price =mData.get(position).getPrice();
                String Item_image =mData.get(position).getImage();
                itemIdList.add(mData.get(position).getId());
                itemCountList.add("1");
                itemPriceList.add(mData.get(position).getSaleprice());
            }
        });

        ViewCartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(mContext, PaymentMethod.class);
                i.putExtra("totalamount",String.valueOf(finalAmmount));
                i.putExtra("productid",itemIdList.toString());
                i.putExtra("count",itemCountList.toString());
                i.putExtra("price",itemPriceList.toString());
                i.putExtra("merchandid",merchandid);
                i.putExtra("table",table);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                mContext.startActivity(i);}
        });





    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {


        TextView sale_price,price,tagline,serve_line,quantity_tv,not_available,food_title;
        ImageView food_image,rest_tagline_logo;
        LinearLayout available_layout;
        CardView add,subtract,quantity_card,add_now_tv;



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
            add_now_tv = itemView.findViewById(R.id.add_now_tv);

        }
    }

}



