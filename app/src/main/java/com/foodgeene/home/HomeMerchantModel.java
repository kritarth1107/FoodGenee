package com.foodgeene.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeMerchantModel {


    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("merchantlist")
    @Expose
    private List<Merchantlist> merchantlist = null;

    @SerializedName("locationlist")
    @Expose
    private List<HomeMerchantModel> locationlist = null;

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;

    public List<HomeMerchantModel> getLocationlist() {
        return locationlist;
    }

    public void setLocationlist(List<HomeMerchantModel> locationlist) {
        this.locationlist = locationlist;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Merchantlist> getMerchantlist() {
        return merchantlist;
    }

    public void setMerchantlist(List<Merchantlist> merchantlist) {
        this.merchantlist = merchantlist;
    }

}
