package com.app.checkinmap.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class help us to handle all the data
 * about the check point to register in the map
 */

public class CheckPointData implements Parcelable{

    private String  mId;
    private String  mName;
    private int     mCheckPointType;
    private double  mLatitude;
    private double  mLongitude;
    private boolean mIsMainTechnical;
    private String  mAddressId;
    private String  mContactId;
    private String  mMainTechnicalId;
    private String  mAddress;
    private String  mContactName;
    private boolean mUpdateAddress;
    private String  mWorkOrderNumber;

    public CheckPointData(){
    }


    protected CheckPointData(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mCheckPointType = in.readInt();
        mLatitude = in.readDouble();
        mLongitude = in.readDouble();
        mIsMainTechnical = in.readByte() != 0;
        mAddressId = in.readString();
        mContactId = in.readString();
        mMainTechnicalId = in.readString();
        mAddress = in.readString();
        mContactName = in.readString();
        mUpdateAddress = in.readByte() != 0;
        mWorkOrderNumber = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mName);
        dest.writeInt(mCheckPointType);
        dest.writeDouble(mLatitude);
        dest.writeDouble(mLongitude);
        dest.writeByte((byte) (mIsMainTechnical ? 1 : 0));
        dest.writeString(mAddressId);
        dest.writeString(mContactId);
        dest.writeString(mMainTechnicalId);
        dest.writeString(mAddress);
        dest.writeString(mContactName);
        dest.writeByte((byte) (mUpdateAddress ? 1 : 0));
        dest.writeString(mWorkOrderNumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CheckPointData> CREATOR = new Creator<CheckPointData>() {
        @Override
        public CheckPointData createFromParcel(Parcel in) {
            return new CheckPointData(in);
        }

        @Override
        public CheckPointData[] newArray(int size) {
            return new CheckPointData[size];
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

    public int getCheckPointType() {
        return mCheckPointType;
    }

    public void setCheckPointType(int checkPointType) {
        this.mCheckPointType = checkPointType;
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

    public boolean isIsMainTechnical() {
        return mIsMainTechnical;
    }

    public void setIsMainTechnical(boolean isMainTechnical) {
        this.mIsMainTechnical = isMainTechnical;
    }

    public String getAddressId() {
        return mAddressId;
    }

    public void setAddressId(String addressId) {
        this.mAddressId = addressId;
    }

    public String getContactId() {
        return mContactId;
    }

    public void setContactId(String contactId) {
        this.mContactId = contactId;
    }

    public String getMainTechnicalId() {
        return mMainTechnicalId;
    }

    public void setMainTechnicalId(String mainTechnicalId) {
        this.mMainTechnicalId = mainTechnicalId;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        this.mAddress = address;
    }

    public String getContactName() {
        return mContactName;
    }

    public void setContactName(String contactName) {
        this.mContactName = contactName;
    }

    public boolean isUpdateAddress() {
        return mUpdateAddress;
    }

    public void setUpdateAddress(boolean updateAddress) {
        this.mUpdateAddress = updateAddress;
    }

    public String getWorkOrderNumber() {
        return mWorkOrderNumber;
    }

    public void setWorkOrderNumber(String workOrderNumber) {
        this.mWorkOrderNumber = workOrderNumber;
    }
}
