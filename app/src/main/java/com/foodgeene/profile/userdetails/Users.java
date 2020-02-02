package com.foodgeene.profile.userdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Users {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("coins")
    @Expose
    private String coins;

    @SerializedName("profilepic")
    @Expose
    private String profilepic;
    @SerializedName("coinburners")
    @Expose
    private List<Users> coinburners = null;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("supportmobile")
    @Expose
    private String supportmobile;

    @SerializedName("supportemail")
    @Expose
    private String supportemail;

    public String getSupportmobile() {
        return supportmobile;
    }

    public void setSupportmobile(String supportmobile) {
        this.supportmobile = supportmobile;
    }

    public String getSupportemail() {
        return supportemail;
    }

    public void setSupportemail(String supportemail) {
        this.supportemail = supportemail;
    }

    public List<Users> getCoinburners() {
        return coinburners;
    }

    public void setCoinburners(List<Users> coinburners) {
        this.coinburners = coinburners;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
