package com.app.checkinmap.model;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 *  This class help us to handle
 *  the data about route
 */

public class Route extends RealmObject {
    @PrimaryKey
    private long   id;
    private String name;
    private String startDate;
    private String endDate;
    private double mileage;
    private String userId;
    private String typeId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getStartDateSalesForceDate() {
        String date ="";
        DateFormat inputDf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        DateFormat ouputDf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000Z");
        try {
            Date checkInDate = inputDf.parse(getStartDate());
            date = ouputDf.format(checkInDate);
            Log.d(" startDate",date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public String getEndDateSalesForceDate() {
        String date ="";
        DateFormat inputDf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        DateFormat ouputDf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000Z");
        try {
            Date checkOutDate = inputDf.parse(getEndDate());
            date = ouputDf.format(checkOutDate);
            Log.d("endDate",date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
