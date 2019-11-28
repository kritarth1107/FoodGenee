package com.foodgeene.transactionlists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.foodgeene.R;
import com.foodgeene.transactionlists.model.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder>{

    List<Text> list;
    Context context;

    public ListAdapter(List<Text> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.coinsreal_row, parent, false);
        ListViewHolder viewHolder = new ListViewHolder(rootView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {

        if(list.get(position).getType().equals("Credit")){

            holder.value.setText("+"+list.get(position).getCoins());

        }
        else if(list.get(position).getType().equals("Debit")){

            holder.value.setText("-"+list.get(position).getCoins());

        }
        holder.description.setText(list.get(position).getReason());
        holder.valDate.setText(list.get(position).getRegdate());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {
        TextView value, description, valDate;
        CircleImageView imageView;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            value = itemView.findViewById(R.id.valueLabe);
            description = itemView.findViewById(R.id.valDesc);
            imageView = itemView.findViewById(R.id.transBag);
            valDate = itemView.findViewById(R.id.valDate);
        }
    }
}
