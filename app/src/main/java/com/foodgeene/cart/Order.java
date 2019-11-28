package com.foodgeene.cart;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class

Order {


    private String unique_id;
    @SerializedName("feedbackstatus")
    @Expose
    private String feedbackstatus;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("coverpic")
    @Expose
    private String coverpic;
    @SerializedName("storename")
    @Expose
    private String storename;
    @SerializedName("tablename")
    @Expose
    private String tablename;
    @SerializedName("totalamount")
    @Expose
    private String totalamount;
    @SerializedName("paymenttype")
    @Expose
    private String paymenttype;
    @SerializedName("orderprocess")
    @Expose
    private String orderprocess;
    @SerializedName("orderprocessstatus")
    @Expose
    private String orderprocessstatus;
    @SerializedName("paidstatus")
    @Expose
    private String paidstatus;
    @SerializedName("products")
    @Expose
    private List<Product> products = null;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCoverpic() {
        return coverpic;
    }

    public void setCoverpic(String coverpic) {
        this.coverpic = coverpic;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public String getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
    }

    public String getPaymenttype() {
        return paymenttype;
    }

    public void setPaymenttype(String paymenttype) {
        this.paymenttype = paymenttype;
    }

    public String getOrderprocess() {
        return orderprocess;
    }

    public void setOrderprocess(String orderprocess) {
        this.orderprocess = orderprocess;
    }

    public String getPaidstatus() {
        return paidstatus;
    }

    public void setPaidstatus(String paidstatus) {
        this.paidstatus = paidstatus;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getFeedbackstatus() {
        return feedbackstatus;
    }

    public void setFeedbackstatus(String feedbackstatus) {
        this.feedbackstatus = feedbackstatus;
    }
    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    public String getOrderprocessstatus() {
        return orderprocessstatus;
    }

    public void setOrderprocessstatus(String orderprocessstatus) {
        this.orderprocessstatus = orderprocessstatus;
    }
}