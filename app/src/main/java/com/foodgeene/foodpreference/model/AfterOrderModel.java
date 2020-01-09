package com.foodgeene.foodpreference.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AfterOrderModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("orders")
    @Expose
    private Orders orders;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }
}
