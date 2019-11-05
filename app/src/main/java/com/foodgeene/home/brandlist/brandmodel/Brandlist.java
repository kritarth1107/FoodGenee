package com.foodgeene.home.brandlist.brandmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Brandlist {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("logo")
    @Expose
    private String logo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

}
