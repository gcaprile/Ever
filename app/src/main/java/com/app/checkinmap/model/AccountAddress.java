package com.app.checkinmap.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * This class help us to handle all data
 * about account´s address
 */

public class AccountAddress implements Parcelable {

    @SerializedName("Id")
    private String  mId;

    @SerializedName("Name")
    private String  mName;

    @SerializedName("Principal__c")
    private boolean mIsPrincipal;

    @SerializedName("Pais__c")
    private String  mCountry;

    @SerializedName("Estado_o_Provincia__c")
    private String  mState;

    @SerializedName("Ciudad__c")
    private String  mCity;

    @SerializedName("Coordenadas__Latitude__s")
    private double  mLatitude;

    @SerializedName("Coordenadas__Longitude__s")
    private double  mLongitude;

    @SerializedName("Cuenta__c")
    private String  mAccountId;

    private boolean  mIsMainTechnical;

    private String  mWorkOrderId;

    public String getWorkOrderId() {
        return mWorkOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.mWorkOrderId = workOrderId;
    }

    public boolean isMainTechnical() {
        return mIsMainTechnical;
    }

    public void setIsMainTechnical(boolean isMainTechnical) {
        this.mIsMainTechnical = isMainTechnical;
    }

    public AccountAddress(){}

    protected AccountAddress(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mIsPrincipal = in.readByte() != 0;
        mCountry = in.readString();
        mState = in.readString();
        mCity = in.readString();
        mLatitude = in.readDouble();
        mLongitude = in.readDouble();
        mAccountId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mName);
        dest.writeByte((byte) (mIsPrincipal ? 1 : 0));
        dest.writeString(mCountry);
        dest.writeString(mState);
        dest.writeString(mCity);
        dest.writeDouble(mLatitude);
        dest.writeDouble(mLongitude);
        dest.writeString(mAccountId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AccountAddress> CREATOR = new Creator<AccountAddress>() {
        @Override
        public AccountAddress createFromParcel(Parcel in) {
            return new AccountAddress(in);
        }

        @Override
        public AccountAddress[] newArray(int size) {
            return new AccountAddress[size];
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

    public boolean isIsPrincipal() {
        return mIsPrincipal;
    }

    public void setIsPrincipal(boolean isPrincipal) {
        this.mIsPrincipal = isPrincipal;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        this.mCountry = country;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        this.mState = state;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        this.mCity = city;
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

    public String getAccountId() {
        return mAccountId;
    }

    public void setAccountId(String accountId) {
        this.mAccountId = accountId;
    }

    public String getAddress(){
        String address = "";


        if(mCity!=null){
            address = address + mCity+", ";
        }

        if(mState!=null){
            address = address + mState+", ";
        }

        if(mCountry!=null){
            address = address + mCountry+", ";
        }
        return address;
    }

}
