package com.app.checkinmap.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * This class help us to handle all the data
 * about work orders
 */

public class WorkOrder implements Parcelable{

    @SerializedName("Id")
    private String mId;

    @SerializedName("WorkOrderNumber")
    private String mWorkOrderNumber;

    @SerializedName("AccountId")
    private String mAccountId;

    @SerializedName("ContactId")
    private String mContactId;

    @SerializedName("Latitude")
    private double mLatitude;

    @SerializedName("Longitude")
    private double mLongitude;

    @SerializedName("Description")
    private String mDescription;

    @SerializedName("StartDate")
    private String mStartDate;

    @SerializedName("EndDate")
    private String mEndDate;

    @SerializedName("Status")
    private String mStatus;

    protected WorkOrder(Parcel in) {
        mId = in.readString();
        mWorkOrderNumber = in.readString();
        mAccountId = in.readString();
        mContactId = in.readString();
        mLatitude = in.readDouble();
        mLongitude = in.readDouble();
        mDescription = in.readString();
        mStartDate = in.readString();
        mEndDate = in.readString();
        mStatus = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mWorkOrderNumber);
        dest.writeString(mAccountId);
        dest.writeString(mContactId);
        dest.writeDouble(mLatitude);
        dest.writeDouble(mLongitude);
        dest.writeString(mDescription);
        dest.writeString(mStartDate);
        dest.writeString(mEndDate);
        dest.writeString(mStatus);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getStartDate() {
        return mStartDate;
    }

    public void setStartDate(String startDate) {
        this.mStartDate = startDate;
    }

    public String getEndDate() {
        return mEndDate;
    }

    public void setEndDate(String endDate) {
        this.mEndDate = endDate;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        this.mStatus = status;
    }


}
