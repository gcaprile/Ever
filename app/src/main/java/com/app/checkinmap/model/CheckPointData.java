package com.app.checkinmap.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * This class help us to handle all the data
 * about the check point to register in the map
 */

public class CheckPointData implements Parcelable{

    @SerializedName("id")
    private String  mId;

    @SerializedName("name")
    private String  mName;

    @SerializedName("check_point_type")
    private int     mCheckPointType;

    @SerializedName("latitude")
    private double  mLatitude;

    @SerializedName("longitude")
    private double  mLongitude;

    @SerializedName("is_main_technical")
    private boolean mIsMainTechnical;

    @SerializedName("address_id")
    private String  mAddressId;

    @SerializedName("contact_id")
    private String  mContactId;

    @SerializedName("main_technical_id")
    private String  mMainTechnicalId;

    @SerializedName("address")
    private String  mAddress;

    @SerializedName("contact_name")
    private String  mContactName;

    @SerializedName("update_address")
    private boolean mUpdateAddress;

    @SerializedName("work_order_number")
    private String  mWorkOrderNumber;

    @SerializedName("user_latitude")
    private double mUserLatitude;

    @SerializedName("user_longitude")
    private double mUserLongitude;

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
        mUserLatitude = in.readDouble();
        mUserLongitude = in.readDouble();
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

    public double getUserLatitude() {
        return mUserLatitude;
    }

    public void setUserLatitude(double userLatitude) {
        this.mUserLatitude = userLatitude;
    }

    public double getUserLongitude() {
        return mUserLongitude;
    }

    public void setUserLongitude(double userLongitude) {
        this.mUserLongitude = userLongitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mName);
        parcel.writeInt(mCheckPointType);
        parcel.writeDouble(mLatitude);
        parcel.writeDouble(mLongitude);
        parcel.writeByte((byte) (mIsMainTechnical ? 1 : 0));
        parcel.writeString(mAddressId);
        parcel.writeString(mContactId);
        parcel.writeString(mMainTechnicalId);
        parcel.writeString(mAddress);
        parcel.writeString(mContactName);
        parcel.writeByte((byte) (mUpdateAddress ? 1 : 0));
        parcel.writeString(mWorkOrderNumber);
        parcel.writeDouble(mUserLatitude);
        parcel.writeDouble(mUserLongitude);
    }
}
