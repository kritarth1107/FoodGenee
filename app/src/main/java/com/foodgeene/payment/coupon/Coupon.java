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
    private Integer totalamt;
    @SerializedName("savingamt")
    @Expose
    private Integer savingamt;

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

    public Integer getTotalamt() {
        return totalamt;
    }

    public void setTotalamt(Integer totalamt) {
        this.totalamt = totalamt;
    }

    public Integer getSavingamt() {
        return savingamt;
    }

    public void setSavingamt(Integer savingamt) {
        this.savingamt = savingamt;
    }

}
