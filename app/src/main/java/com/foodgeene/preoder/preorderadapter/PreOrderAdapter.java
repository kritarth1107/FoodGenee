package com.foodgeene.preoder.preorderadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.foodgeene.R;
import com.foodgeene.preoder.preordermodel.Tablelist;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class PreOrderAdapter extends RecyclerView.Adapter<PreOrderAdapter.PreOrderViewHolder> {

    List<Tablelist> list;
    Context context;
    BookClickListener clickListener;
    String type;

    public PreOrderAdapter(List<Tablelist> list, Context context, BookClickListener clickListener,String type) {
        this.list = list;
        this.context = context;
        this.clickListener = clickListener;
        this.type=type;
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

        if(type.equalsIgnoreCase("table")) {
            holder.mLLTable.setVisibility(View.VISIBLE);
            holder.mIvGallery.setVisibility(View.GONE);
            holder.tableNumber.setText(list.get(position).getName());
            holder.mTvCapacity.setText("Capacity : " + list.get(position).getCapacity());
        }else {
            holder.mLLTable.setVisibility(View.GONE);
            holder.mIvGallery.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(list.get(position).getImage())
                    .centerCrop()
                    .into(holder.mIvGallery);
            holder.mIvGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    AlertDialog dialog = builder.create();
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    View dialogLayout = inflater.inflate(R.layout.full_view, null);
                    dialog.setView(dialogLayout);
                    ImageView iv=dialogLayout.findViewById(R.id.iv_dialog_view);
                    ImageView close=dialogLayout.findViewById(R.id.iv_close);
                    Glide.with(context)
                            .load(list.get(position).getImage())
                            .centerCrop()
                            .into(iv);
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                    dialog.show();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PreOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tableNumber,mTvCapacity;
        TextView bookTable;
        BookClickListener clickListener;
        ImageView mIvGallery;
        LinearLayout mLLTable;
        public PreOrderViewHolder(@NonNull View itemView, BookClickListener clickListener) {
            super(itemView);
            this.clickListener = clickListener;
            tableNumber = itemView.findViewById(R.id.realTableNo);
            bookTable = itemView.findViewById(R.id.bookTableButton);
            mTvCapacity=itemView.findViewById(R.id.tv_capacity);
            mIvGallery=itemView.findViewById(R.id.im_gallery);
            mLLTable=itemView.findViewById(R.id.ll_table);
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
