package com.app.checkinmap.util;


import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private static final String PREFERENCE_SERVICES_STARTED =       "service_started";
    private static final String PREFERENCE_IS_SELLER =              "is_seller";
    private static final String PREFERENCE_IS_IN_ROUTE =            "is_in_route";
    private static final String PREFERENCE_IS_DOING_CHECK_IN =      "is_doing_check_in";
    private static final String PREFERENCE_ROUTE_ID =               "route_id";
    private static final String PREFERENCE_RADIUS =                 "radius";
    private static final String PREFERENCE_CHECK_POINT_DATA =       "check_point_data";
    private static final String PREFERENCE_CHECK_POINT_LOCATION =   "check_point_location";

    private static PreferenceManager mInstance;
    private SharedPreferences mPreferences;

    public static PreferenceManager getInstance(Context context){
        if(mInstance == null){
            mInstance = new PreferenceManager(context);
        }

        return mInstance;
    }

    private PreferenceManager(Context context){
        mPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setIsServiceEnabled(boolean isStarted){
        mPreferences.edit().putBoolean(PREFERENCE_SERVICES_STARTED, isStarted).apply();
    }

    public boolean isServiceEnabled(){
        return mPreferences.getBoolean(PREFERENCE_SERVICES_STARTED, false);
    }

    public void setIsSeller(boolean isSeller){
        mPreferences.edit().putBoolean(PREFERENCE_IS_SELLER, isSeller).apply();
    }

    public boolean isSeller(){
        return mPreferences.getBoolean(PREFERENCE_IS_SELLER, true);
    }

    public void setIsInRoute(boolean isInRoute){
        mPreferences.edit().putBoolean(PREFERENCE_IS_IN_ROUTE, isInRoute).apply();
    }

    public boolean isInRoute(){
        return mPreferences.getBoolean(PREFERENCE_IS_IN_ROUTE, false);
    }

    public void setRouteId(long routeId){
        mPreferences.edit().putLong(PREFERENCE_ROUTE_ID, routeId).apply();
    }

    public long getRouteId(){
        return mPreferences.getLong(PREFERENCE_ROUTE_ID,0);
    }

    public void setRadius(int radius){
        mPreferences.edit().putInt(PREFERENCE_RADIUS, radius).apply();
    }

    public long getRadius(){
        return mPreferences.getInt(PREFERENCE_RADIUS,500);
    }

    public void setIsDoingCheckIn(boolean isDoingCheckIn){
        mPreferences.edit().putBoolean(PREFERENCE_IS_DOING_CHECK_IN, isDoingCheckIn).apply();
    }

    public boolean isDoingCheckIn(){
        return mPreferences.getBoolean(PREFERENCE_IS_DOING_CHECK_IN, false);
    }

    public void setCheckPointData(String checkPointData){
        mPreferences.edit().putString(PREFERENCE_CHECK_POINT_DATA, checkPointData).apply();
    }

    public String getCheckPointData(){
        return mPreferences.getString(PREFERENCE_CHECK_POINT_DATA, null);
    }

    public void setCheckPointLocation(String checkPointLocation){
        mPreferences.edit().putString(PREFERENCE_CHECK_POINT_LOCATION, checkPointLocation).apply();
    }

    public String getCheckPointLocation(){
        return mPreferences.getString(PREFERENCE_CHECK_POINT_LOCATION, null);
    }
}
