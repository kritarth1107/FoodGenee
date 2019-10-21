package com.foodgeene.register;

public class RegisterModel {
    String usersid,text,status;
    public RegisterModel(){}

    public RegisterModel(String usersid, String text, String status) {
        this.usersid = usersid;
        this.text = text;
        this.status = status;
    }

    public String getUsersid() {
        return usersid;
    }

    public void setUsersid(String usersid) {
        this.usersid = usersid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
