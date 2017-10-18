package com.app.checkinmap.model;

import android.util.Log;

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
    @PrimaryKey
    private long   id;
    private double checkInLatitude;
    private double checkInLongitude;
    private double checkOutLatitude;
    private double checkOutLongitude;
    private double latitude;
    private double longitude;
    private String leadId;
    private String workOrderContactId;
    private String accountContactId;
    private String addressId;
    private String checkInDate;
    private String checkOutDate;
    private String visitTime;
    private String travelTime;
    private String workOrderId;
    private String visitType;
    private String description;
    private String routeId;
    private String name;
    private String technicalId;
    private String recordType;
    private String accountContactName;
    private String address;
    private double visitTimeNumber;
    private double travelTimeNumber;
    private boolean updateAddress;
    private boolean isMainTechnical;
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
