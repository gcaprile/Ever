package com.app.checkinmap.util;


import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private static final String PREFERENCE_SERVICES_STARTED = "service_started";

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
}
