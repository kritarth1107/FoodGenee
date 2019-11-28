package com.foodgeene.cart;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.foodgeene.MainActivity;
import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.home.HomeAdapter;
import com.foodgeene.home.HomeMerchantModel;
import com.foodgeene.home.Merchantlist;
import com.foodgeene.restraunt.RestrauntActivity;
import com.foodgeene.restraunt.RestrauntAdapter;
import com.foodgeene.scanner.Productlist;
import com.foodgeene.scanner.ScannerModel;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import network.FoodGeneeAPI;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**

 */
public class Orders extends Fragment implements RatingDialogListener {
    RecyclerView cart_recyclerView;
    ShimmerFrameLayout shimmer_view_container;
    LinearLayout NorOrderFound;
    MainActivity mainActivity;
    SessionManager sessionManager;
    public Orders() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_orders, container, false);
        cart_recyclerView = rootView.findViewById(R.id.cart_recyclerView);
        sessionManager = new SessionManager(getContext());
        shimmer_view_container = rootView.findViewById(R.id.shimmer_view_container);
        NorOrderFound = rootView.findViewById(R.id.NorOrderFound);
        setupRecyclerView();

        return rootView;
    }

    private void setupRecyclerView() {
        shimmer_view_container.setVisibility(View.VISIBLE);
        shimmer_view_container.startShimmerAnimation();


        HashMap<String, String> user = sessionManager.getUserDetail();
        String userToken = user.get(sessionManager.USER_ID);

        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
        Call<OrderListModel> call = foodGeneeAPI.GetOrderList("orderlist",userToken,"application/x-www-form-urlencoded");
        call.enqueue(new Callback<OrderListModel>() {
            @Override
            public void onResponse(Call<OrderListModel> call, Response<OrderListModel> response) {
                try {
                        List<Order> orderDetails = response.body().getOrders();
                        OrderlistAdapter orderlistAdapter = new OrderlistAdapter(getContext(), orderDetails, sessionManager, userToken);
                        int  x = orderlistAdapter.getItemCount();
                        if(x>0){
                            cart_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            cart_recyclerView.setAdapter(orderlistAdapter);
                            shimmer_view_container.setVisibility(View.GONE);
                            shimmer_view_container.stopShimmerAnimation();
                        }
                        else{
                            NorOrderFound.setVisibility(View.VISIBLE);
                        }
                }
                catch (Exception e){
                    NorOrderFound.setVisibility(View.VISIBLE);
                    shimmer_view_container.setVisibility(View.GONE);
                    shimmer_view_container.stopShimmerAnimation();
                }

            }
            @Override
            public void onFailure(Call<OrderListModel> call, Throwable t) {
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
                shimmer_view_container.setVisibility(View.GONE);
                shimmer_view_container.stopShimmerAnimation();
            }
        });

    }






    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onNeutralButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(int i, @NotNull String s) {

    }
}
