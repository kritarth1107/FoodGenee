package com.foodgeene.updateprofile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateModel {

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("text")
    @Expose
    private String text;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
