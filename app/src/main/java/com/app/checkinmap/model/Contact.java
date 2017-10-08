package com.app.checkinmap.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * This class help us to handle all the
 * contact information
 */

public class Contact implements Parcelable {

    @SerializedName("Id")
    private String mId;

    @SerializedName("Name")
    private String mName;

    @SerializedName("Phone")
    private String mPhone;

    @SerializedName("MobilePhone")
    private String mMobilePhone;

    @SerializedName("Email")
    private String mEmail;

    @SerializedName("AccountId")
    private String mAccountId;

    protected Contact(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mPhone = in.readString();
        mMobilePhone = in.readString();
        mEmail = in.readString();
        mAccountId = in.readString();
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
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

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        this.mPhone = phone;
    }

    public String getMobilePhone() {
        return mMobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mMobilePhone = mobilePhone;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public String getAccountId() {
        return mAccountId;
    }

    public void setAccountId(String accountId) {
        this.mAccountId = accountId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mName);
        parcel.writeString(mPhone);
        parcel.writeString(mMobilePhone);
        parcel.writeString(mEmail);
        parcel.writeString(mAccountId);
    }
}
