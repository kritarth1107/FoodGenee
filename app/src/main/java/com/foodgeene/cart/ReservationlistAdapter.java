package com.foodgeene.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReservationlistAdapter extends RecyclerView.Adapter<ReservationlistAdapter.HomeViewHolder> {

    List<Reservation> list;
    Context context;
    RequestOptions option;
    SessionManager sessionManager;
    String userToken;



    public ReservationlistAdapter(Context context, List<Reservation> list, SessionManager sessionManager, String userToken) {
        this.list = list;
        this.context = context;
        this.sessionManager = sessionManager;
        this.userToken = userToken;
        option = new RequestOptions().centerCrop().placeholder(R.drawable.background).error(R.drawable.background);

    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.reservation_row, parent, false);
        HomeViewHolder homeViewHolder = new HomeViewHolder(rootView);

        return homeViewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {

        
        holder.Restraunt_Name.setText("Store Name : "+list.get(position).getStorename());
        holder.TableNumber.setText("Table Name : "+list.get(position).getTable());
        holder.status.setText("Status : "+list.get(position).getStatustext());
        holder.tv_date.setText("Date : "+list.get(position).getBookdate()+" "+list.get(position).getBooktime());

    }



    @Override
    public int getItemCount() {
            return list.size();
    }





    public class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView Restraunt_Name,TableNumber,tv_date,status;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            Restraunt_Name = itemView.findViewById(R.id.tv_store_name);
            tv_date=itemView.findViewById(R.id.tv_time);
            TableNumber = itemView.findViewById(R.id.tv_table_name);
            status=itemView.findViewById(R.id.tv_status);



        }
    }
}
