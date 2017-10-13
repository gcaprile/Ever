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

    @SerializedName("Tecnico__c")
    private String mTechnicalId;

    @SerializedName("Principal__c")
    private boolean mIsPrincipal;

    @SerializedName("Work_Order__c")
    private String mWorkOrderId;

    @SerializedName("Work_Order__r")
    private WorkOrderDetail mWorkOrderDetail;

    protected WorkOrder(Parcel in) {
        mId = in.readString();
        mTechnicalId = in.readString();
        mIsPrincipal = in.readByte() != 0;
        mWorkOrderId = in.readString();
        mWorkOrderDetail = in.readParcelable(WorkOrderDetail.class.getClassLoader());
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

    public String getTechnicalId() {
        return mTechnicalId;
    }

    public void setTechnicalId(String technicalId) {
        this.mTechnicalId = technicalId;
    }

    public boolean isIsPrincipal() {
        return mIsPrincipal;
    }

    public void setIsPrincipal(boolean isPrincipal) {
        this.mIsPrincipal = isPrincipal;
    }

    public String getWorkOrderId() {
        return mWorkOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.mWorkOrderId = workOrderId;
    }

    public WorkOrderDetail getWorkOrderDetail() {
        return mWorkOrderDetail;
    }

    public void setWorkOrderDetail(WorkOrderDetail workOrderDetail) {
        this.mWorkOrderDetail = workOrderDetail;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mTechnicalId);
        parcel.writeByte((byte) (mIsPrincipal ? 1 : 0));
        parcel.writeString(mWorkOrderId);
        parcel.writeParcelable(mWorkOrderDetail, i);
    }
}
