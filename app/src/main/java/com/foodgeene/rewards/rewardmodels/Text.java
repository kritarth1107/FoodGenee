package com.foodgeene.rewards.rewardmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Text implements Serializable {


    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("coins")
    @Expose
    private String coins;
    @SerializedName("validityfrom")
    @Expose
    private String validityfrom;
    @SerializedName("validityto")
    @Expose
    private String validityto;
    @SerializedName("couponcode")
    @Expose
    private String couponcode;
    @SerializedName("excerpt")
    @Expose
    private String excerpt;
    @SerializedName("cover")
    @Expose
    private String cover;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("soldout")
    @Expose
    private String soldout;

    public String getSoldout() {
        return soldout;
    }

    public void setSoldout(String soldout) {
        this.soldout = soldout;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }

    public String getValidityfrom() {
        return validityfrom;
    }

    public void setValidityfrom(String validityfrom) {
        this.validityfrom = validityfrom;
    }

    public String getValidityto() {
        return validityto;
    }

    public void setValidityto(String validityto) {
        this.validityto = validityto;
    }

    public String getCouponcode() {
        return couponcode;
    }

    public void setCouponcode(String couponcode) {
        this.couponcode = couponcode;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
