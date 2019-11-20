package com.foodgeene.redeemedlistdetails.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RedeemedModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("text")
    @Expose
    private Text text;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }
}
