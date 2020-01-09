package com.foodgeene.preoder.preordermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tablelist {

    @SerializedName("tableid")
    @Expose
    private String tableid;
    @SerializedName("name")
    @Expose
    private String name;

    public String getTableid() {
        return tableid;
    }

    public void setTableid(String tableid) {
        this.tableid = tableid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}