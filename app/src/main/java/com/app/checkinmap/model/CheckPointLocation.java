package com.app.checkinmap.model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * This class help us to handle
 * the check in and check out user
 * information
 */

public class CheckPointLocation extends RealmObject {
    @SerializedName("id")
    @PrimaryKey
    private long   id;

    @SerializedName("check_in_latitude")
    private double checkInLatitude;

    @SerializedName("check_in_longitude")
    private double checkInLongitude;

    @SerializedName("check_out_latitude")
    private double checkOutLatitude;

    @SerializedName("check_out_longitude")
    private double checkOutLongitude;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("lead_id")
    private String leadId;

    @SerializedName("work_order_contact_id")
    private String workOrderContactId;

    @SerializedName("account_contact_id")
    private String accountContactId;

    @SerializedName("address_id")
    private String addressId;

    @SerializedName("check_in_date")
    private String checkInDate;

    @SerializedName("check_out_date")
    private String checkOutDate;

    @SerializedName("visit_time")
    private String visitTime;

    @SerializedName("travel_time")
    private String travelTime;

    @SerializedName("work_order_id")
    private String workOrderId;

    @SerializedName("visit_type")
    private String visitType;

    @SerializedName("description")
    private String description;

    @SerializedName("route_id")
    private String routeId;

    @SerializedName("name")
    private String name;

    @SerializedName("technical_id")
    private String technicalId;

    @SerializedName("record_type")
    private String recordType;

    @SerializedName("account_contact_name")
    private String accountContactName;

    @SerializedName("address")
    private String address;

    @SerializedName("visit_time_number")
    private double visitTimeNumber;

    @SerializedName("travel_time_number")
    private double travelTimeNumber;

    @SerializedName("update_address")
    private boolean updateAddress;

    @SerializedName("is_main_technical")
    private boolean isMainTechnical;

    @SerializedName("signature_file_path")
    private String  signatureFilePath;

    public String getSignatureFilePath() {
        return signatureFilePath;
    }

    public void setSignatureFilePath(String signatureFilePath) {
        this.signatureFilePath = signatureFilePath;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isUpdateAddress() {
        return updateAddress;
    }

    public void setUpdateAddress(boolean updateAddress) {
        this.updateAddress = updateAddress;
    }

    public double getVisitTimeNumber() {
        return visitTimeNumber;
    }

    public void setVisitTimeNumber(double visitTimeNumber) {
        this.visitTimeNumber = visitTimeNumber;
    }

    public double getTravelTimeNumber() {
        return travelTimeNumber;
    }

    public void setTravelTimeNumber(double travelTimeNumber) {
        this.travelTimeNumber = travelTimeNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getCheckInLatitude() {
        return checkInLatitude;
    }

    public void setCheckInLatitude(double checkInLatitude) {
        this.checkInLatitude = checkInLatitude;
    }

    public double getCheckInLongitude() {
        return checkInLongitude;
    }

    public void setCheckInLongitude(double checkInLongitude) {
        this.checkInLongitude = checkInLongitude;
    }

    public double getCheckOutLatitude() {
        return checkOutLatitude;
    }

    public void setCheckOutLatitude(double checkOutLatitude) {
        this.checkOutLatitude = checkOutLatitude;
    }

    public double getCheckOutLongitude() {
        return checkOutLongitude;
    }

    public void setCheckOutLongitude(double checkOutLongitude) {
        this.checkOutLongitude = checkOutLongitude;
    }

    public String getLeadId() {
        return leadId;
    }

    public void setLeadId(String leadId) {
        this.leadId = leadId;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getWorkOrderContactId() {
        return workOrderContactId;
    }

    public void setWorkOrderContactId(String workOrderContactId) {
        this.workOrderContactId = workOrderContactId;
    }

    public String getAccountContactId() {
        return accountContactId;
    }

    public void setAccountContactId(String accountContactId) {
        this.accountContactId = accountContactId;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(String visitTime) {
        this.visitTime = visitTime;
    }

    public String getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(String travelTime) {
        this.travelTime = travelTime;
    }

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getVisitType() {
        return visitType;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTechnicalId() {
        return technicalId;
    }

    public void setTechnicalId(String technicalId) {
        this.technicalId = technicalId;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getAccountContactName() {
        return accountContactName;
    }

    public void setAccountContactName(String accountContactName) {
        this.accountContactName = accountContactName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isMainTechnical() {
        return isMainTechnical;
    }

    public void setMainTechnical(boolean mainTechnical) {
        isMainTechnical = mainTechnical;
    }

    public String getCheckInDateSalesForceDate() {
        String date ="";
        DateFormat inputDf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        DateFormat ouputDf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000Z");
        try {
            Date checkInDate = inputDf.parse(getCheckInDate());
            date = ouputDf.format(checkInDate);
            Log.d("inDate",date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public String getCheckOutDateSalesForceDate() {
        String date ="";
        DateFormat inputDf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        DateFormat ouputDf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000Z");
        try {
            Date checkOutDate = inputDf.parse(getCheckOutDate());
            date = ouputDf.format(checkOutDate);
            Log.d("outDate",date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
