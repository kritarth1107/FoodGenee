package com.foodgeene.payment.coupon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coupon {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("coupanamt")
    @Expose
    private String coupanamt;
    @SerializedName("totalamt")
    @Expose
    private String totalamt;
    @SerializedName("savingamt")
    @Expose
    private String savingamt;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCoupanamt() {
        return coupanamt;
    }

    public void setCoupanamt(String coupanamt) {
        this.coupanamt = coupanamt;
    }

    public String getTotalamt() {
        return totalamt;
    }

    public void setTotalamt(String totalamt) {
        this.totalamt = totalamt;
    }

    public String getSavingamt() {
        return savingamt;
    }

    public void setSavingamt(String savingamt) {
        this.savingamt = savingamt;
    }
}