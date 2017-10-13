package com.app.checkinmap.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ASUS-PC on 12/10/2017.
 */

public class WorkOrderAddress implements Parcelable{

    @SerializedName("Id")
    private String mId;

    @SerializedName("Direccion__c")
    private String mStreet;

    @SerializedName("Ciudad__c")
    private String mCity;

    @SerializedName("Estado_o_Provincia__c")
    private String mState;

    @SerializedName("Pais__c")
    private String mCountry;

    @SerializedName("Coordenadas__c")
    private WorkOrderLatLng mCoordinates;

    protected WorkOrderAddress(Parcel in) {
        mId = in.readString();
        mStreet = in.readString();
        mCity = in.readString();
        mState = in.readString();
        mCountry = in.readString();
        mCoordinates = in.readParcelable(WorkOrderLatLng.class.getClassLoader());
    }

    public static final Creator<WorkOrderAddress> CREATOR = new Creator<WorkOrderAddress>() {
        @Override
        public WorkOrderAddress createFromParcel(Parcel in) {
            return new WorkOrderAddress(in);
        }

        @Override
        public WorkOrderAddress[] newArray(int size) {
            return new WorkOrderAddress[size];
        }
    };

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getStreet() {
        return mStreet;
    }

    public void setStreet(String street) {
        this.mStreet = street;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        this.mCity = city;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        this.mState = state;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        this.mCountry = country;
    }

    public WorkOrderLatLng getCoordinates() {
        return mCoordinates;
    }

    public void setCoordinates(WorkOrderLatLng coordinates) {
        this.mCoordinates = coordinates;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mStreet);
        parcel.writeString(mCity);
        parcel.writeString(mState);
        parcel.writeString(mCountry);
        parcel.writeParcelable(mCoordinates, i);
    }

    public String getAddress(){
        String address = "";
        if(mStreet!=null){
            address = address +mStreet+", ";
        }

        if(mCity!=null){
            address = address +mCity+", ";
        }

        if(mState!=null){
            address = address +mState+", ";
        }

        if(mCountry!=null){
            address = address +mCountry;
        }

        return address;
    }
}
