package com.foodgeene.profile.userdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("users")
    @Expose
    private Users users;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {

        this.users = users;
    }

}
