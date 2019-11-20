package com.foodgeene.coinstransactions.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Transaction {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("text")
    @Expose
    private List<Text> text = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Text> getText() {
        return text;
    }

    public void setText(List<Text> text) {
        this.text = text;
    }

}
