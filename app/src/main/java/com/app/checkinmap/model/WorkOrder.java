package com.app.checkinmap.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * This class help us to handle all the data
 * about work orders
 */

public class WorkOrder  implements Parcelable{

    @SerializedName("Id")
    private String mId;

    @SerializedName("WorkOrderNumber")
    private String mWorkOrderNumber;

    @SerializedName("Country")
    private String mCountry;

    @SerializedName("Cuenta_del__c")
    private String mAccountName;

    @SerializedName("Contacto__c")
    private String mContactName;

    @SerializedName("Detalle_Direccion__c")
    private String mAddressDetail;

    @SerializedName("Direccion_Visita__c")
    private String mAddressId;

    @SerializedName("AccountId")
    private String mAccountId;

    @SerializedName("ContactId")
    private String mContactId;

    @SerializedName("Latitude")
    private double mLatitude;

    @SerializedName("Longitude")
    private double mLongitude;

    @SerializedName("Status")
    private String mStatus;

    protected WorkOrder(Parcel in) {
        mId = in.readString();
        mWorkOrderNumber = in.readString();
        mCountry = in.readString();
        mAccountName = in.readString();
        mContactName = in.readString();
        mAddressDetail = in.readString();
        mAddressId = in.readString();
        mAccountId = in.readString();
        mContactId = in.readString();
        mLatitude = in.readDouble();
        mLongitude = in.readDouble();
        mStatus = in.readString();
    }

    public static final Creator<WorkOrder> CREATOR = new Creator<WorkOrder>() {
        @Override
        public WorkOrder createFromParcel(Parcel in) {
            return new WorkOrder(in);
        }

        @Override
        public WorkOrder[] newArray(int size) {
            return new WorkOrder[size];
        }
    };

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getWorkOrderNumber() {
        return mWorkOrderNumber;
    }

    public void setWorkOrderNumber(String workOrderNumber) {
        this.mWorkOrderNumber = workOrderNumber;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        this.mCountry = country;
    }

    public String getAccountName() {
        return mAccountName;
    }

    public void setAccountName(String accountName) {
        this.mAccountName = accountName;
    }

    public String getContactName() {
        return mContactName;
    }

    public void setContactName(String contactName) {
        this.mContactName = contactName;
    }

    public String getAddressDetail() {
        return mAddressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.mAddressDetail = addressDetail;
    }

    public String getAddressId() {
        return mAddressId;
    }

    public void setAddressId(String addressId) {
        this.mAddressId = addressId;
    }

    public String getAccountId() {
        return mAccountId;
    }

    public void setAccountId(String accountId) {
        this.mAccountId = accountId;
    }

    public String getContactId() {
        return mContactId;
    }

    public void setContactId(String contactId) {
        this.mContactId = contactId;
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

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        this.mStatus = status;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mWorkOrderNumber);
        parcel.writeString(mCountry);
        parcel.writeString(mAccountName);
        parcel.writeString(mContactName);
        parcel.writeString(mAddressDetail);
        parcel.writeString(mAddressId);
        parcel.writeString(mAccountId);
        parcel.writeString(mContactId);
        parcel.writeDouble(mLatitude);
        parcel.writeDouble(mLongitude);
        parcel.writeString(mStatus);
    }
}
