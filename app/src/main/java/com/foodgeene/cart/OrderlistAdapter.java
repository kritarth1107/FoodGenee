package com.foodgeene.cart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.foodgeene.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderlistAdapter extends RecyclerView.Adapter<OrderlistAdapter.HomeViewHolder> {

    List<Order> list;
    Context context;
    RequestOptions option;


    public OrderlistAdapter(Context context,List<Order> list) {
        this.list = list;
        this.context = context;
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
        holder.Restraunt_Name.setText(list.get(position).getStorename());
        holder.OrderID.setText(list.get(position).getOrderId());
        holder.TableNumber.setText(list.get(position).getTablename());
        holder.TotalAmount.setText(list.get(position).getTotalamount());
        holder.PaymentMethod.setText(list.get(position).getPaymenttype());
        holder.OrderStatus.setText(list.get(position).getOrderprocess());
        holder.PaymentStatus.setText(list.get(position).getPaidstatus());
        ArrayList arrayList = new ArrayList<>(list.get(position).getProducts());
//        List<Product> l = list.get(position).getProducts();
//        List<Product> newList = new ArrayList<>()
//        for(Product product: l){
//            newList.add(product);
//        }


        holder.OrderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Product> products = new ArrayList<Product>(list.get(position).getProducts());

                Intent intent = new Intent(context,OrderDetails.class);
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST",(Serializable) products);
                intent.putExtra("BUNDLE",args);
                intent.putExtra("Restraunt",list.get(position).getStorename());
                intent.putExtra("id",list.get(position).getOrderId());
                intent.putExtra("paymentMethod",list.get(position).getPaymenttype());
                intent.putExtra("orderStatus",list.get(position).getOrderprocess());
                intent.putExtra("paymentStatus",list.get(position).getPaidstatus());
                intent.putExtra("totalAmount",list.get(position).getTotalamount());
                intent.putExtra("tableName",list.get(position).getTablename());


                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
            return list.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView Restraunt_Name,OrderID,TableNumber,TotalAmount,PaymentMethod,OrderStatus,PaymentStatus;
        CardView OrderCard;
        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            Restraunt_Name = itemView.findViewById(R.id.Restraunt_Name);
            OrderID = itemView.findViewById(R.id.OrderID);
            TableNumber = itemView.findViewById(R.id.TableNumber);
            TotalAmount = itemView.findViewById(R.id.TotalAmount);
            PaymentMethod = itemView.findViewById(R.id.PaymentMethod);
            OrderStatus = itemView.findViewById(R.id.OrderStatus);
            PaymentStatus = itemView.findViewById(R.id.PaymentStatus);
            OrderCard = itemView.findViewById(R.id.OrderCard);

        }
    }
}
