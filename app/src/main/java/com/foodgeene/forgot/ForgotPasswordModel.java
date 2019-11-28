package com.foodgeene.forgot;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgotPasswordModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("usersid")
    @Expose
    private String usersid;
    @SerializedName("text")
    @Expose
    private String text;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsersid() {
        return usersid;
    }

    public void setUsersid(String usersid) {
        this.usersid = usersid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


}
