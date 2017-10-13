package com.app.checkinmap.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ASUS-PC on 12/10/2017.
 */

public class WorkOrderDetail implements Parcelable{

    @SerializedName("WorkOrderNumber")
    private String mWorkOrderNumber;

    @SerializedName("Cuenta_del__c")
    private String mContactAccountName;

    @SerializedName("AccountId")
    private String mContactAccountId;

    @SerializedName("Contacto__c")
    private String mContactName;

    @SerializedName("ContactId")
    private String mContactId;

    @SerializedName("Status")
    private String mStatus;

    @SerializedName("Direccion_Visita__r")
    private WorkOrderAddress mWorkOrderAddress;

    protected WorkOrderDetail(Parcel in) {
        mWorkOrderNumber = in.readString();
        mContactAccountName = in.readString();
        mContactAccountId = in.readString();
        mContactName = in.readString();
        mContactId = in.readString();
        mStatus = in.readString();
        mWorkOrderAddress = in.readParcelable(WorkOrderAddress.class.getClassLoader());
    }

    public static final Creator<WorkOrderDetail> CREATOR = new Creator<WorkOrderDetail>() {
        @Override
        public WorkOrderDetail createFromParcel(Parcel in) {
            return new WorkOrderDetail(in);
        }

        @Override
        public WorkOrderDetail[] newArray(int size) {
            return new WorkOrderDetail[size];
        }
    };

    public String getWorkOrderNumber() {
        return mWorkOrderNumber;
    }

    public void setWorkOrderNumber(String workOrderNumber) {
        this.mWorkOrderNumber = workOrderNumber;
    }

    public String getContactAccountName() {
        return mContactAccountName;
    }

    public void setContactAccountName(String contactAccountName) {
        this.mContactAccountName = contactAccountName;
    }

    public String getContactAccountId() {
        return mContactAccountId;
    }

    public void setContactAccountId(String contactAccountId) {
        this.mContactAccountId = contactAccountId;
    }

    public String getContactName() {
        return mContactName;
    }

    public void setContactName(String contactName) {
        this.mContactName = contactName;
    }

    public String getContactId() {
        return mContactId;
    }

    public void setContactId(String contactId) {
        this.mContactId = contactId;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        this.mStatus = status;
    }

    public WorkOrderAddress getWorkOrderAddress() {
        return mWorkOrderAddress;
    }

    public void setWorkOrderAddress(WorkOrderAddress workOrderAddress) {
        this.mWorkOrderAddress = workOrderAddress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mWorkOrderNumber);
        parcel.writeString(mContactAccountName);
        parcel.writeString(mContactAccountId);
        parcel.writeString(mContactName);
        parcel.writeString(mContactId);
        parcel.writeString(mStatus);
        parcel.writeParcelable(mWorkOrderAddress, i);
    }
}
