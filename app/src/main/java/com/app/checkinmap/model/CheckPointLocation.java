package com.app.checkinmap.model;

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
}
