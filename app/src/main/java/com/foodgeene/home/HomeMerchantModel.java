package com.foodgeene.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeMerchantModel {


    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("merchantlist")
    @Expose
    private List<Merchantlist> merchantlist = null;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Merchantlist> getMerchantlist() {
        return merchantlist;
    }

    public void setMerchantlist(List<Merchantlist> merchantlist) {
        this.merchantlist = merchantlist;
    }

}
