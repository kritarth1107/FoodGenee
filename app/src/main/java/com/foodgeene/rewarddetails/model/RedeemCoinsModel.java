package com.foodgeene.rewarddetails.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RedeemCoinsModel {

    @SerializedName("status")
    private String statusO;
    @SerializedName("message")
    private String message0;
    @SerializedName("couponcode")


    private String couponcode0;

    public String getStatusO() {
        return statusO;
    }

    public void setStatusO(String statusO) {
        this.statusO = statusO;
    }

    public String getMessage0() {
        return message0;
    }

    public void setMessage0(String message0) {
        this.message0 = message0;
    }

    public String getCouponcode0() {
        return couponcode0;
    }

    public void setCouponcode0(String couponcode0) {
        this.couponcode0 = couponcode0;
    }
}
