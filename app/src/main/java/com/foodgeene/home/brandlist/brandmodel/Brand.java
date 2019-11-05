package com.foodgeene.home.brandlist.brandmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Brand {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("brandlist")
    @Expose
    private List<Brandlist> brandlist = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Brandlist> getBrandlist() {
        return brandlist;
    }

    public void setBrandlist(List<Brandlist> brandlist) {
        this.brandlist = brandlist;
    }
}
