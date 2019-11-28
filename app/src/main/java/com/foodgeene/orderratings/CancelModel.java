package com.foodgeene.orderratings;

import com.google.gson.annotations.SerializedName;

public class CancelModel {
    @SerializedName("status")
    public String status;
    @SerializedName("message")
    public String text;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
