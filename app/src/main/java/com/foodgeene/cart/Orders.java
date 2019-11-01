package com.foodgeene.cart;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.home.HomeAdapter;
import com.foodgeene.home.HomeMerchantModel;
import com.foodgeene.home.Merchantlist;
import com.foodgeene.restraunt.RestrauntActivity;
import com.foodgeene.restraunt.RestrauntAdapter;
import com.foodgeene.scanner.Productlist;
import com.foodgeene.scanner.ScannerModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import network.FoodGeneeAPI;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**

 */
public class Orders extends Fragment {
    RecyclerView cart_recyclerView;

    public Orders() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_orders, container, false);
        cart_recyclerView = rootView.findViewById(R.id.cart_recyclerView);
        setupRecyclerView();

        return rootView;
    }

    private void setupRecyclerView() {
        SessionManager sessionManager = new SessionManager(getContext());
        HashMap<String, String> user = sessionManager.getUserDetail();
        String userToken = user.get(sessionManager.USER_ID);

        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
        Call<Orderlist> call = foodGeneeAPI.GetOrderList("orderlist",userToken,"application/x-www-form-urlencoded");
        call.enqueue(new Callback<Orderlist>() {
            @Override
            public void onResponse(Call<Orderlist> call, Response<Orderlist> response) {
                try {
                    String status = response.body().getStatus().trim();
                    if (status.equals("1")) {
                        List<Order> order = response.body().getOrders();

                        OrderlistAdapter orderlistAdapter = new OrderlistAdapter(getContext(), order);
                        cart_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        cart_recyclerView.setAdapter(orderlistAdapter);


                    } else if (status.equals("0")) {
                    }


                }
                catch (Exception e){
                }

            }
            @Override
            public void onFailure(Call<Orderlist> call, Throwable t) {
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}
