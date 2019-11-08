package com.foodgeene.home.brandlist.brandmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Brand {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("bannerlist")
    @Expose
    private List<Bannerlist> bannerlist = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Bannerlist> getBannerlist() {
        return bannerlist;
    }

    public void setBannerlist(List<Bannerlist> bannerlist) {
        this.bannerlist = bannerlist;
    }
}
