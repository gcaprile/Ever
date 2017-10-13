package com.app.checkinmap.util;

import android.util.Log;

import com.app.checkinmap.model.UserLocation;
import com.salesforce.androidsdk.rest.RestClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * This class help us encapsulate
 * all the common method in the application
 */

public class Utility {
    public static final String TAG="emasal";
    private static RestClient  mRestClient;
    private static String      mUserProfileId;
    private static Roles       mUserRole;
    private static String      mUserProfileName;
    private static String      mUserCountry;

    public enum Roles{
        SELLER,
        TECHNICAL,
        OTHER
    }

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
     * This method set the user profile id like a global variable
     */
    public static void setUserProfileId(String userProfileId){
        mUserProfileId = userProfileId;
        /*Here we check if the user is a seller*/
        switch (userProfileId){
            case "00e6A000000IRoOQAW":
                setUserRole(Roles.SELLER);
                break;
            case "00e6A000000IRoEQAW":
                setUserRole(Roles.TECHNICAL);
                break;
            default:
                setUserRole(Roles.OTHER);
                break;
        }
    }

    /**
     * This method get the user profile id like
     */
    public static String getUserProfileId(){
        return mUserProfileId ;
    }

    /**
     * This method set the user type*/
    public static void setUserRole(Roles userRole){
        mUserRole = userRole;
    }

    /**
     * This method get the user type
     */
    public static Roles getUserRole(){
        return mUserRole ;
    }


    /**
     * This method get the user profile name
     */
    public static String getUserProfileName(){
        return mUserProfileName ;
    }

    /**
     * This method set the user profile name*/
    public static void setUserProfileName(String userProfileName){
        mUserProfileName = userProfileName;
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

    /**
     * This method get the user country
     */
    public static String getUserCountry(){
        return mUserCountry ;
    }

    /**
     * This method set the user country*/
    public static void setUserCountry(String userCountry){
        mUserCountry = userCountry;
    }

    /**
     * This method help us to get the current
     * date with hours for route name
     */
    public static String getDateForName(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * This method help us to get the current
     * date with hours for route search
     */
    public static String getDateForSearch(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * This method help us to get the time in hour
     * for the visit
     */
    public static String getDurationInHours(String dateStart,String dateFinish){
        String time ="";

        //HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        Date d1;
        Date d2;

        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateFinish);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();

           // long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
           // long diffDays = diff / (24 * 60 * 60 * 1000);

            time = diffHours+" horas "+diffMinutes+" min";

        } catch (Exception e) {
            e.printStackTrace();
        }

        return time;
    }
}
