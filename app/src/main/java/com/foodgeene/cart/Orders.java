package com.foodgeene.cart;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.foodgeene.MainActivity;
import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import network.ConnectivityReceiver;
import network.FoodGeneeAPI;
import network.MyApplication;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**

 */
public class Orders extends Fragment implements RatingDialogListener, ConnectivityReceiver.ConnectivityReceiverListener {
    RecyclerView cart_recyclerView;
    ShimmerFrameLayout shimmer_view_container;
    LinearLayout NorOrderFound;
    MainActivity mainActivity;
    SessionManager sessionManager;
    TextView mTvOrders,mTvTable;
    View mVOrder,mVTable;
    TextView mTvMsg;
    boolean isOnLine;

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
        mTvOrders=rootView.findViewById(R.id.tv_orders);
        mTvTable=rootView.findViewById(R.id.tv_table);
        mTvMsg=rootView.findViewById(R.id.tv_msg);
        isOnLine=ConnectivityReceiver.isConnected();

        mVOrder=rootView.findViewById(R.id.v_order);
        mVTable=rootView.findViewById(R.id.v_table);

        mVTable.setVisibility(View.GONE);
        mVOrder.setVisibility(View.VISIBLE);
        if(isOnLine)
        setupRecyclerView();
        else Toast.makeText(getActivity(), "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();

        mTvOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mVTable.setVisibility(View.GONE);
                mVOrder.setVisibility(View.VISIBLE);
                if(isOnLine) {
                    cart_recyclerView.setVisibility(View.VISIBLE);
                    NorOrderFound.setVisibility(View.GONE);
                    setupRecyclerView();
                }else Toast.makeText(getActivity(), "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();
            }
        });

        mTvTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mVTable.setVisibility(View.VISIBLE);
                mVOrder.setVisibility(View.GONE);
                if(isOnLine) {
                    cart_recyclerView.setVisibility(View.VISIBLE);
                    NorOrderFound.setVisibility(View.GONE);
                    setupReservationList();
                }else Toast.makeText(getActivity(), "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();
            }
        });


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mVTable.setVisibility(View.GONE);
        mVOrder.setVisibility(View.VISIBLE);
        MyApplication.getInstance().setConnectivityListener(this);
        if (isOnLine)
        setupRecyclerView();
        else Toast.makeText(getActivity(), "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();
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
                            cart_recyclerView.setVisibility(View.VISIBLE);
                            cart_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            cart_recyclerView.setAdapter(orderlistAdapter);
                            shimmer_view_container.setVisibility(View.GONE);
                            shimmer_view_container.stopShimmerAnimation();
                        }
                        else{
                            NorOrderFound.setVisibility(View.VISIBLE);
                            cart_recyclerView.setVisibility(View.GONE);
                            mTvMsg.setText("It looks you haven't placed any order yet.");
                        }
                }
                catch (Exception e){
                    NorOrderFound.setVisibility(View.VISIBLE);
                    cart_recyclerView.setVisibility(View.GONE);
                    shimmer_view_container.setVisibility(View.GONE);
                    shimmer_view_container.stopShimmerAnimation();
                    mTvMsg.setText("It looks you haven't placed any order yet.");
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



    private void setupReservationList() {
        shimmer_view_container.setVisibility(View.VISIBLE);
        shimmer_view_container.startShimmerAnimation();


        HashMap<String, String> user = sessionManager.getUserDetail();
        String userToken = user.get(sessionManager.USER_ID);

        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
        Call<ReservationListModel> call = foodGeneeAPI.GetReservationList("reservationlist",userToken,"application/x-www-form-urlencoded");
        call.enqueue(new Callback<ReservationListModel>() {
            @Override
            public void onResponse(Call<ReservationListModel> call, Response<ReservationListModel> response) {
                try {

                    List<Reservation> orderDetails = response.body().getReservationlist();
                    ReservationlistAdapter orderlistAdapter = new ReservationlistAdapter(getContext(), orderDetails, sessionManager, userToken);
                    int  x = orderlistAdapter.getItemCount();
                    if(x>0){
                        cart_recyclerView.setVisibility(View.VISIBLE);
                        NorOrderFound.setVisibility(View.GONE);
                        cart_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        cart_recyclerView.setAdapter(orderlistAdapter);
                        shimmer_view_container.setVisibility(View.GONE);
                        shimmer_view_container.stopShimmerAnimation();
                    }
                    else{
                        NorOrderFound.setVisibility(View.VISIBLE);
                        cart_recyclerView.setVisibility(View.GONE);
                        mTvMsg.setText("It looks you haven't placed any table yet.");
                    }
                }
                catch (Exception e){
                    NorOrderFound.setVisibility(View.VISIBLE);
                    cart_recyclerView.setVisibility(View.GONE);
                    shimmer_view_container.setVisibility(View.GONE);
                    shimmer_view_container.stopShimmerAnimation();
                    mTvMsg.setText("It looks you haven't placed any table yet.");
                }

            }
            @Override
            public void onFailure(Call<ReservationListModel> call, Throwable t) {
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

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isOnLine=isConnected;
    }
}
