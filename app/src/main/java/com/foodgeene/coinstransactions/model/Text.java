package com.foodgeene.coinstransactions.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Text {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("validityto")
    @Expose
    private String validityto;
    @SerializedName("excerpt")
    @Expose
    private String excerpt;
    @SerializedName("logo")
    @Expose
    private String logo;

    @SerializedName("reason")
    @Expose
    private String reason;

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

    public String getValidityto() {
        return validityto;
    }

    public void setValidityto(String validityto) {
        this.validityto = validityto;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
