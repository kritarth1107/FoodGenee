package com.foodgeene.scanner;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Productlist {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("unique_id")
    @Expose
    private String uniqueId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("labeltag")
    @Expose
    private String labeltag;
    @SerializedName("serveline")
    @Expose
    private String serveline;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("saleprice")
    @Expose
    private String saleprice;
    @SerializedName("availabilty")
    @Expose
    private String availabilty;
    @SerializedName("image")
    @Expose
    private String image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLabeltag() {
        return labeltag;
    }

    public void setLabeltag(String labeltag) {
        this.labeltag = labeltag;
    }

    public String getServeline() {
        return serveline;
    }

    public void setServeline(String serveline) {
        this.serveline = serveline;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSaleprice() {
        return saleprice;
    }

    public void setSaleprice(String saleprice) {
        this.saleprice = saleprice;
    }

    public String getAvailabilty() {
        return availabilty;
    }

    public void setAvailabilty(String availabilty) {
        this.availabilty = availabilty;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}