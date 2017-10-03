package com.app.checkinmap.util;

import android.util.Log;

import com.salesforce.androidsdk.rest.RestClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class help us encapsulate
 * all the common method in the application
 */

public class Utility {
    public static final String TAG="emasal";
    private static RestClient  mRestClient;

    /**
     * This method help us to get the current
     * date with hours
     */
    public static String getCurrentDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * This method set the Rest client instance from the login
     */
    public static void setRestClient(RestClient restClient){
        mRestClient = restClient;
    }

    /**
     * This method set the Rest client instance from the login
     */
    public static RestClient getRestClient(){
        return mRestClient;
    }

    /**
     * This method help us to
     * show the large JSON
     */
    public static void logLargeString(String str) {
        if(str.length() > 3000) {
            Log.i(TAG, str.substring(0, 3000));
            logLargeString(str.substring(3000));
        } else {
            Log.i(TAG, str); // continuation
        }
    }
}
