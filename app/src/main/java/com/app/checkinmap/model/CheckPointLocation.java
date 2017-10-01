package com.app.checkinmap.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * This class help us to handle
 * the check in and check out user
 * information
 */

public class CheckPointLocation extends RealmObject {
    @PrimaryKey
    private long   id;
    private double checkInLatitude;
    private double checkInLongitude;
    private double checkOutLatitude;
    private double checkOutLongitude;
    private String CheckInDate;
    private String CheckOutDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getCheckInLatitude() {
        return checkInLatitude;
    }

    public void setCheckInLatitude(double checkInLatitude) {
        this.checkInLatitude = checkInLatitude;
    }

    public double getCheckInLongitude() {
        return checkInLongitude;
    }

    public void setCheckInLongitude(double checkInLongitude) {
        this.checkInLongitude = checkInLongitude;
    }

    public double getCheckOutLatitude() {
        return checkOutLatitude;
    }

    public void setCheckOutLatitude(double checkOutLatitude) {
        this.checkOutLatitude = checkOutLatitude;
    }

    public double getCheckOutLongitude() {
        return checkOutLongitude;
    }

    public void setCheckOutLongitude(double checkOutLongitude) {
        this.checkOutLongitude = checkOutLongitude;
    }

    public String getCheckInDate() {
        return CheckInDate;
    }

    public void setCheckInDate(String checkInDate) {
        CheckInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return CheckOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        CheckOutDate = checkOutDate;
    }
}
