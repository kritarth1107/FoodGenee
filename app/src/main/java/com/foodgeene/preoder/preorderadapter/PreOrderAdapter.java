package com.foodgeene.preoder.preorderadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.foodgeene.R;
import com.foodgeene.preoder.preordermodel.PreOrderModel;
import com.foodgeene.preoder.preordermodel.Tablelist;

import java.util.List;

import retrofit2.Callback;

public class PreOrderAdapter extends RecyclerView.Adapter<PreOrderAdapter.PreOrderViewHolder> {

    List<Tablelist> list;
    Context context;
    BookClickListener clickListener;

    public PreOrderAdapter(List<Tablelist> list, Context context, BookClickListener clickListener) {
        this.list = list;
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public PreOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.preorder_row, parent, false);
        PreOrderViewHolder preOrderViewHolder = new PreOrderViewHolder(view, clickListener);
        return preOrderViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PreOrderViewHolder holder, int position) {

        holder.tableNumber.setText(list.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PreOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tableNumber;
        Button bookTable;
        BookClickListener clickListener;
        public PreOrderViewHolder(@NonNull View itemView, BookClickListener clickListener) {
            super(itemView);
            this.clickListener = clickListener;
            tableNumber = itemView.findViewById(R.id.realTableNo);
            bookTable = itemView.findViewById(R.id.bookTableButton);
            bookTable.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(bookTable.getId() == v.getId()){

                clickListener.buttonClick(getAdapterPosition());
            }
        }
    }


    public interface BookClickListener{

         void buttonClick(int position);
    }
}
