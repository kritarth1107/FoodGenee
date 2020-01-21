package com.foodgeene.cart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReservationListModel {

    @SerializedName("reservationlist")
    @Expose
    private List<Reservation> reservationlist = null;

    public List<Reservation> getReservationlist() {
        return reservationlist;
    }

    public void setReservationlist(List<Reservation> reservationlist) {
        this.reservationlist = reservationlist;
    }
}