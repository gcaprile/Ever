package com.app.checkinmap.bus;


/**
 * This class provide new events about new locations
 * receives.
 */
public class NewLocationEvent {

    private double mLat;
    private double mLon;

    public void setLat(double lat){
        this.mLat = lat;
    }

    public double getLat(){
        return mLat;
    }

    public double getLon(){
        return mLon;
    }

    public void setLon(double lon){
        this.mLon = lon;
    }
}