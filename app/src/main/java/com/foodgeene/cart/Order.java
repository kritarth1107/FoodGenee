package com.foodgeene.cart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

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

    @Expose
    @SerializedName("rating")
    private Integer rating;

    @Expose
    @SerializedName("orderdate")
    private String orderdate;

    @Expose
    @SerializedName("serviceboy")
    private String serviceboy;
    @Expose
    @SerializedName("orderprocesstext")
    private String orderprocesstext;
    @Expose
    @SerializedName("enckey")
    private String enckey;
    @Expose
    @SerializedName("preparetime")
    private String preparetime;

    @Expose
    @SerializedName("merchant_id")
    private String merchant_id;

    @Expose
    @SerializedName("couponamount")
    private String couponamount;
    @Expose
    @SerializedName("verify")
    private String verify;

    @Expose
    @SerializedName("tax")
    private String tax;

    @Expose
    @SerializedName("tips")
    private String tips;

    @Expose
    @SerializedName("subscription")
    private String subscription;


    @Expose
    @SerializedName("showaddmore")
    private String showaddmore;


    public String getShowaddmore() {
        return showaddmore;
    }

    public void setShowaddmore(String showaddmore) {
        this.showaddmore = showaddmore;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getSubscription() {
        return subscription;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public String getCouponamount() {
        return couponamount;
    }

    public void setCouponamount(String couponamount) {
        this.couponamount = couponamount;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getPreparetime() {
        return preparetime;
    }

    public void setPreparetime(String preparetime) {
        this.preparetime = preparetime;
    }

    public String getEnckey() {
        return enckey;
    }

    public void setEnckey(String enckey) {
        this.enckey = enckey;
    }

    public String getOrderprocesstext() {
        return orderprocesstext;
    }

    public void setOrderprocesstext(String orderprocesstext) {
        this.orderprocesstext = orderprocesstext;
    }

    public String getServiceboy() {
        return serviceboy;
    }

    public void setServiceboy(String serviceboy) {
        this.serviceboy = serviceboy;
    }

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

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public void setOrderprocessstatus(String orderprocessstatus) {
        this.orderprocessstatus = orderprocessstatus;
    }
}