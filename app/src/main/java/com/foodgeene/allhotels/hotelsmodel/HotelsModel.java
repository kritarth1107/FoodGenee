package com.foodgeene.allhotels.hotelsmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HotelsModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("merchantlist")
    @Expose
    private List<Merchantlist> merchantlist = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Merchantlist> getMerchantlist() {
        return merchantlist;
    }

    public void setMerchantlist(List<Merchantlist> merchantlist) {
        this.merchantlist = merchantlist;
    }

}
