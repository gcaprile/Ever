package com.app.checkinmap.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class help us encapsulate
 * all the common method in the application
 */

public class Utility {

    /**
     * This method help us to get the current
     * date with hours
     */
    public static String getCurrentDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
