package com.app.checkinmap.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ASUS-PC on 12/10/2017.
 */

public class WorkOrderLatLng implements Parcelable{

    @SerializedName("latitude")
    private double mLatitude;

    @SerializedName("longitude")
    private double mLongitude;

    protected WorkOrderLatLng(Parcel in) {
        mLatitude = in.readDouble();
        mLongitude = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(mLatitude);
        dest.writeDouble(mLongitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WorkOrderLatLng> CREATOR = new Creator<WorkOrderLatLng>() {
        @Override
        public WorkOrderLatLng createFromParcel(Parcel in) {
            return new WorkOrderLatLng(in);
        }

        @Override
        public WorkOrderLatLng[] newArray(int size) {
            return new WorkOrderLatLng[size];
        }
    };

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
}
