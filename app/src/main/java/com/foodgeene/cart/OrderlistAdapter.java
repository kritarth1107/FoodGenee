package com.foodgeene.cart;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.orderratings.RatingsActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import network.FoodGeneeAPI;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderlistAdapter extends RecyclerView.Adapter<OrderlistAdapter.HomeViewHolder> {

    List<Order> list;
    Context context;
    RequestOptions option;
    SessionManager sessionManager;
    String userToken;



    public OrderlistAdapter(Context context,List<Order> list, SessionManager sessionManager, String userToken) {
        this.list = list;
        this.context = context;
        this.sessionManager = sessionManager;
        this.userToken = userToken;
        option = new RequestOptions().centerCrop().placeholder(R.drawable.background).error(R.drawable.background);

    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.order_row, parent, false);
        HomeViewHolder homeViewHolder = new HomeViewHolder(rootView);

        return homeViewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {

        if(list.get(position).getOrderprocessstatus().equals("1")){

            holder.orderProcessStatus.setVisibility(View.VISIBLE);
            holder.orderProcessStatus.setOnClickListener(view -> {
                FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
                Call<AlertModel> call = foodGeneeAPI.alertIcon("set-alert", list.get(position).getOrderId(), userToken, "application/x-www-form-urlencoded");
                call.enqueue(new Callback<AlertModel>() {
                    @Override
                    public void onResponse(Call<AlertModel> call, Response<AlertModel> response) {
                        try {
                            AlertModel retrievedModel = response.body();
                            if(retrievedModel.getStatus().equals("1")){
                                Toast.makeText(context, retrievedModel.message, Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e){
                            Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<AlertModel> call, Throwable t) {
                        Toast.makeText(context, "", Toast.LENGTH_SHORT).show();


                    }
                });

            });
        }

        Glide.with(context)
                .load(list.get(position).getLogo())
                .into(holder.orderRestImage);
        
        holder.Restraunt_Name.setText(list.get(position).getStorename());
        holder.OrderID.setText(list.get(position).getOrderId());
        holder.TableNumber.setText(list.get(position).getTablename());
        holder.TotalAmount.setText("₹ "+list.get(position).getTotalamount());
        holder.PaymentMethod.setText(list.get(position).getPaymenttype());
        holder.OrderStatus.setText(list.get(position).getOrderprocesstext());
        holder.PaymentStatus.setText(list.get(position).getPaidstatus());
        holder.tv_date.setText(list.get(position).getOrderdate());

//        Glide.with(context)
//               .load(list.get(position).getOrderprocess());

        ArrayList arrayList = new ArrayList<>(list.get(position).getProducts());
//        List<Product> l = list.get(position).getProducts();
//        List<Product> newList = new ArrayList<>()
//        for(Product product: l){
//            newList.add(product);
//        }



        if(list.get(position).getFeedbackstatus().equals("false")){
            String orderId = list.get(position).getOrderId();

            if(list.get(position).getOrderprocess().equalsIgnoreCase("2")||list.get(position).getOrderprocess().equalsIgnoreCase("4")){
                holder.rateHere.setText("Rate us");
                holder.rateHere.setVisibility(View.VISIBLE);
                holder.rateHere.setBackground(context.getResources().getDrawable(
                        R.drawable.ratingborder));
                holder.rateHere.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                holder.rateHere.setTextColor(Color.WHITE);
                    holder.rateHere.setOnClickListener(view -> {
                        holder.rateHere.setVisibility(View.GONE);
                        Intent intent = new Intent(context, RatingsActivity.class);
                        intent.putExtra("orderId", orderId);
                        intent.putExtra("merchant", list.get(position).getMerchant_id());
                        context.startActivity(intent);
                    });

            }else{
                holder.rateHere.setVisibility(View.GONE);
            }



        } else if(list.get(position).getFeedbackstatus().equals("true")){
            //Log.e("rating",""+list.get(position).getRating());
            holder.rateHere.setVisibility(View.VISIBLE);
           // holder.rateHere.setVisibility(View.GONE);
            holder.rateHere.setText(""+list.get(position).getRating());
            holder.rateHere.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.rateHere.setTextColor(Color.parseColor("#364760"));
            holder.rateHere.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.star,0);

        }
//        holder.rateHere.setOnClickListener(view -> {
//
//           Intent intent = new Intent(context, RatingsActivity.class);
//           context.startActivity(intent);
//
//        });

        holder.OrderCard.setOnClickListener(view -> {
            List<Product> products = new ArrayList<Product>(list.get(position).getProducts());

            Intent intent = new Intent(context,OrderDetails.class);
            Bundle args = new Bundle();
            args.putSerializable("ARRAYLIST",(Serializable) products);
            intent.putExtra("BUNDLE",args);
            intent.putExtra("Restraunt",list.get(position).getStorename());
            intent.putExtra("id",list.get(position).getUnique_id());
            intent.putExtra("paymentMethod",list.get(position).getPaymenttype());
            intent.putExtra("orderStatus",list.get(position).getOrderprocess());
            intent.putExtra("paymentStatus",list.get(position).getPaidstatus());
            intent.putExtra("totalAmount",list.get(position).getTotalamount());
            intent.putExtra("tableName",list.get(position).getTablename());
            intent.putExtra("serviceBoy",list.get(position).getServiceboy());
            intent.putExtra("encKey",list.get(position).getEnckey());
            intent.putExtra("cover",list.get(position).getLogo());
            intent.putExtra("orderID",list.get(position).getOrderId());
            intent.putExtra("prePaidTime",list.get(position).getPreparetime());
            intent.putExtra("couponAmount",list.get(position).getCouponamount());
            intent.putExtra("verify",list.get(position).getVerify());
            intent.putExtra("showaddmore",list.get(position).getShowaddmore());
            intent.putExtra("tip",list.get(position).getTips());
            intent.putExtra("tax",list.get(position).getTax());
            intent.putExtra("subscription",list.get(position).getSubscription());

            context.startActivity(intent);
        });
    }



    @Override
    public int getItemCount() {
            return list.size();
    }





    public class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView Restraunt_Name,OrderID,TableNumber,TotalAmount,PaymentMethod,OrderStatus,PaymentStatus,tv_date;
        CardView OrderCard;
        ImageView orderRestImage;
        TextView rateHere;
        CardView orderProcessStatus;
        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            Restraunt_Name = itemView.findViewById(R.id.Restraunt_Name);
            OrderID = itemView.findViewById(R.id.OrderID);
            tv_date=itemView.findViewById(R.id.tv_date);
            TableNumber = itemView.findViewById(R.id.TableNumber);
            TotalAmount = itemView.findViewById(R.id.TotalAmount);
            PaymentMethod = itemView.findViewById(R.id.PaymentMethod);
            OrderStatus = itemView.findViewById(R.id.OrderStatus);
            PaymentStatus = itemView.findViewById(R.id.PaymentStatus);
            OrderCard = itemView.findViewById(R.id.OrderCard);
            orderRestImage = itemView.findViewById(R.id.storeName);
            rateHere = itemView.findViewById(R.id.rateThisOrder);
            orderProcessStatus = itemView.findViewById(R.id.orderprocesslayout);



        }
    }
}
