package com.foodgeene.foodpreference.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.foodgeene.R;
import com.foodgeene.foodpreference.model.AfterOrderModel;
import com.foodgeene.foodpreference.model.Product;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import network.FoodGeneeAPI;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AfterOrderAdapter extends RecyclerView.Adapter<AfterOrderAdapter.AfterViewHolder> {

    List<Product> list;
    List<String> preference;
    String orderId,userToken;
    Context context;

    public AfterOrderAdapter(List<Product> list, String orderId, String userToken, Context context) {
        this.list = list;
        this.orderId=orderId;
        this.userToken=userToken;
        this.context=context;
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
        holder.prefernce.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(s.length()>0){
                    FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
                    Call<AfterOrderModel> call = foodGeneeAPI.submitPreference("arrangeorder", orderId,list.get(position).getId(),s.toString(),userToken,"application/x-www-form-urlencoded");
                    call.enqueue(new Callback<AfterOrderModel>() {
                        @Override
                        public void onResponse(Call<AfterOrderModel> call, Response<AfterOrderModel> response) {

                            try{

                                if(response.body().getStatus().equals("1")){
                                    Toast.makeText(context,"Updated Successfully",Toast.LENGTH_SHORT).show();

                                }
                                else if(response.body().getStatus().equals("0")){

                                }

                            }
                            catch (Exception e){
                                Toast.makeText(context,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<AfterOrderModel> call, Throwable t) {

                        }
                    });
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


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
