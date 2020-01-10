package com.foodgeene.foodpreference.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.foodgeene.R;
import com.foodgeene.foodpreference.model.Product;

import java.util.ArrayList;
import java.util.List;

public class AfterOrderAdapter extends RecyclerView.Adapter<AfterOrderAdapter.AfterViewHolder> {

    List<Product> list;
    List<String> preference;

    public AfterOrderAdapter(List<Product> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public AfterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_prefen, parent, false);
        AfterViewHolder afterViewHolder = new AfterViewHolder(view);
        return afterViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AfterViewHolder holder, int position) {

        holder.products.setText(list.get(position).getName());
        holder.prefernce.setText(String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AfterViewHolder extends RecyclerView.ViewHolder {
        TextView products;
        EditText prefernce;
        public AfterViewHolder(@NonNull View itemView) {
            super(itemView);

            products = itemView.findViewById(R.id.foodSet);
            prefernce = itemView.findViewById(R.id.spinnerSet);

        }
    }


    public interface SavePreferenceClick{

        void saveClick();
    }
}
