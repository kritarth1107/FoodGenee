package com.foodgeene.transactionlists.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Text {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("coins")
    @Expose
    private String coins;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("regdate")
    @Expose
    private String regdate;
    @SerializedName("reason")
    @Expose
    private String reason;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
