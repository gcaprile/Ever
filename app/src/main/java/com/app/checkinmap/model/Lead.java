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

    @SerializedName("Company")
    private String mCompany;

    @SerializedName("Country")
    private String mCountry;

    @SerializedName("Latitude")
    private double mLatitude;

    @SerializedName("Longitude")
    private double mLongitude;

    @SerializedName("Address")
    private String mAddress;

    @SerializedName("Phone")
    private String mPhone;

    @SerializedName("Website")
    private String mWebsite;

    @SerializedName("Email")
    private String mEmail;

    @SerializedName("Description")
    private String mDescription;


    protected Lead(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mCompany = in.readString();
        mCountry = in.readString();
        mLatitude = in.readDouble();
        mLongitude = in.readDouble();
        mAddress = in.readString();
        mPhone = in.readString();
        mWebsite = in.readString();
        mEmail = in.readString();
        mDescription = in.readString();
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

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
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

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        this.mAddress = address;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        this.mPhone = phone;
    }

    public String getWebsite() {
        return mWebsite;
    }

    public void setWebsite(String website) {
        this.mWebsite = website;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mName);
        parcel.writeString(mCompany);
        parcel.writeString(mCountry);
        parcel.writeDouble(mLatitude);
        parcel.writeDouble(mLongitude);
        parcel.writeString(mAddress);
        parcel.writeString(mPhone);
        parcel.writeString(mWebsite);
        parcel.writeString(mEmail);
        parcel.writeString(mDescription);
    }
}
