package com.app.checkinmap.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * This class help us to encapsulate all the
 * account information
 */

public class Account  implements Parcelable{
    @SerializedName("Id")
    private String mId;

    @SerializedName("Name")
    private String mName;

    @SerializedName("Phone")
    private String mPhone;

    @SerializedName("Emasal_Address__c")
    private String mAddress;

    @SerializedName("Pais__c")
    private String mCountry;

    @SerializedName("Description")
    private String mDescription;


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

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        this.mPhone = phone;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        this.mAddress = address;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        this.mCountry = country;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    protected Account(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mPhone = in.readString();
        mAddress = in.readString();
        mCountry = in.readString();
        mDescription = in.readString();
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
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
        parcel.writeString(mPhone);
        parcel.writeString(mAddress);
        parcel.writeString(mCountry);
        parcel.writeString(mDescription);
    }
}
