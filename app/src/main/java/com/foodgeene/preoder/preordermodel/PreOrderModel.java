package com.foodgeene.preoder.preordermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PreOrderModel {


    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("storename")
    @Expose
    private String storename;
    @SerializedName("storetype")
    @Expose
    private String storetype;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("coverpic")
    @Expose
    private String coverpic;
    @SerializedName("tablelist")
    @Expose
    private List<Tablelist> tablelist = null;
    @SerializedName("gallerylist")
    @Expose
    private List<Tablelist> gallerylist = null;



    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("description")
    @Expose
    private String description;

    public List<Tablelist> getGallerylist() {
        return gallerylist;
    }

    public void setGallerylist(List<Tablelist> gallerylist) {
        this.gallerylist = gallerylist;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public String getStoretype() {
        return storetype;
    }

    public void setStoretype(String storetype) {
        this.storetype = storetype;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
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

    public List<Tablelist> getTablelist() {
        return tablelist;
    }

    public void setTablelist(List<Tablelist> tablelist) {
        this.tablelist = tablelist;
    }
}
