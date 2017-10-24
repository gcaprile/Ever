package com.app.checkinmap.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * This class help us to handle all the
 * lead data
 */

public class Lead implements Parcelable{

    @SerializedName("Id")
    private String mId;

    @SerializedName("Name")
    private String mName;

    @SerializedName("Phone")
    private String mPhone;

    @SerializedName("Company")
    private String mCompany;

    @SerializedName("Street")
    private String mStreet;

    @SerializedName("City")
    private String mCity;

    @SerializedName("State")
    private String mState;

    @SerializedName("PostalCode")
    private String mPostalCode;

    @SerializedName("Country")
    private String mCountry;

    @SerializedName("Coordenadas__Latitude__s")
    private double mLatitude;

    @SerializedName("Coordenadas__Longitude__s")
    private double mLongitude;

    protected Lead(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mCompany = in.readString();
        mStreet = in.readString();
        mCity = in.readString();
        mState = in.readString();
        mPostalCode = in.readString();
        mCountry = in.readString();
        mLatitude = in.readDouble();
        mLongitude = in.readDouble();
        mPhone = in.readString();
    }

    public static final Creator<Lead> CREATOR = new Creator<Lead>() {
        @Override
        public Lead createFromParcel(Parcel in) {
            return new Lead(in);
        }

        @Override
        public Lead[] newArray(int size) {
            return new Lead[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mName);
        parcel.writeString(mCompany);
        parcel.writeString(mStreet);
        parcel.writeString(mCity);
        parcel.writeString(mState);
        parcel.writeString(mPostalCode);
        parcel.writeString(mCountry);
        parcel.writeDouble(mLatitude);
        parcel.writeDouble(mLongitude);
        parcel.writeString(mPhone);
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getCompany() {
        return mCompany;
    }

    public void setCompany(String company) {
        this.mCompany = company;
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

    public String getPostalCode() {
        return mPostalCode;
    }

    public void setPostalCode(String postalCode) {
        this.mPostalCode = postalCode;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        this.mCountry = country;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        this.mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        this.mLongitude = longitude;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        this.mPhone = phone;
    }

    public String getAddress(){
        String address = "";
        if(mStreet!=null){
            address = address + mStreet+", ";
        }

        if(mCity!=null){
            address = address + mCity+", ";
        }

        if(mState!=null){
            address = address + mState+", ";
        }

        if(mPostalCode!=null){
            address = address + mPostalCode+", ";
        }


        if(mCountry!=null){
            address = address + mCountry;
        }
        return address;
    }
}
