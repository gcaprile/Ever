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

    @SerializedName("BillingStreet")
    private String mStreet;

    @SerializedName("BillingCity")
    private String mCity;

    @SerializedName("BillingState")
    private String mState;

    @SerializedName("BillingPostalCode")
    private String mPostalCode;

    @SerializedName("BillingCountry")
    private String mCountry;

    @SerializedName("Description")
    private String mDescription;

    @SerializedName("Numero_Contactos__c")
    private int mNumberContacts;

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

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public int getNumberContacts() {
        return mNumberContacts;
    }

    public void setNumberContacts(int numberContacts) {
        this.mNumberContacts = numberContacts;
    }

    public String getAddress(){
        String address ="";
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

    protected Account(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mPhone = in.readString();
        mStreet = in.readString();
        mCity = in.readString();
        mState = in.readString();
        mPostalCode = in.readString();
        mCountry = in.readString();
        mDescription = in.readString();
        mNumberContacts = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mName);
        dest.writeString(mPhone);
        dest.writeString(mStreet);
        dest.writeString(mCity);
        dest.writeString(mState);
        dest.writeString(mPostalCode);
        dest.writeString(mCountry);
        dest.writeString(mDescription);
        dest.writeInt(mNumberContacts);
    }

    @Override
    public int describeContents() {
        return 0;
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
}
