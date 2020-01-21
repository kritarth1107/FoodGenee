package com.foodgeene.cart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Reservation {

    @Expose
    @SerializedName("merchant_id")
    private String merchant_id;
    @Expose
    @SerializedName("storename")
    private String storename;
    @Expose
    @SerializedName("table")
    private String table;
    @Expose
    @SerializedName("bookdate")
    private String bookdate;
    @Expose
    @SerializedName("booktime")
    private String booktime;
    @Expose
    @SerializedName("status")
    private String status;
    @Expose
    @SerializedName("statustext")
    private String statustext;

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getBookdate() {
        return bookdate;
    }

    public void setBookdate(String bookdate) {
        this.bookdate = bookdate;
    }

    public String getBooktime() {
        return booktime;
    }

    public void setBooktime(String booktime) {
        this.booktime = booktime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatustext() {
        return statustext;
    }

    public void setStatustext(String statustext) {
        this.statustext = statustext;
    }
}
