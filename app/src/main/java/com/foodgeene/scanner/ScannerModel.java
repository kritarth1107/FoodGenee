package com.foodgeene.scanner;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScannerModel {


    @SerializedName("tablename")
    @Expose
    private String tablename;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("merchantid")
    @Expose
    private String merchantid;
    @SerializedName("table")
    @Expose
    private String table;
    @SerializedName("store")
    @Expose
    private String store;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("coverpic")
    @Expose
    private String coverpic;
    @SerializedName("productlist")
    @Expose
    private List<Productlist> productlist = null;
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

    public String getMerchantid() {
        return merchantid;
    }

    public void setMerchantid(String merchantid) {
        this.merchantid = merchantid;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
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

    public List<Productlist> getProductlist() {
        return productlist;
    }

    public void setProductlist(List<Productlist> productlist) {
        this.productlist = productlist;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }
}