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
    @SerializedName("capacity")
    @Expose
    private String capacity;

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

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