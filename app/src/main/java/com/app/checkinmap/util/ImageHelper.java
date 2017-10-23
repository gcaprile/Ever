package com.app.checkinmap.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class help us to convert the image
 * to BASE64 string and compress the image if
 * in necessary
 */

public class ImageHelper {
    public static final String      PICTURE_DIRECTORY="EmasalApp";
    private static String mCurrentPhotoPath;

    /**
     * This method help us to transform the image in
     * a BASE64 string
     */
    public static String getBase64FromImage(String imageFilePath){
        Bitmap bm = BitmapFactory.decodeFile(imageFilePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }


    /**
     * This method help us to save the current bitmap
     */
    public static File saveBitMap(Bitmap bitmap,String workOrderId){
        File file=null;
        try {

            /*double percentSize = 0.75;

            double newHeight = bitmap.getHeight() - (bitmap.getHeight()*percentSize);
            double newWidth =  bitmap.getWidth() -  (bitmap.getWidth()*percentSize);

            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int)newWidth, (int)newHeight, false);*/

            file = createImageFile(workOrderId);

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

            //write the bytes in file
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(bytes.toByteArray());

            // remember close de FileOutput
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * This method help us to create a file in order
     * to save the image captured from cam.
     */
    public static File createImageFile(String workOrderId) throws IOException {
        //Here we define the image file
        File imageFile=null;

        // Create an image file name
        // String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "firma_electronica_"+workOrderId+".jpg";

        //create a file to write bitmap data
        File directory= new File(Environment.getExternalStorageDirectory(),PICTURE_DIRECTORY);

        //Here we verify if exist the directory
        if(!directory.exists()){
            directory.mkdir();
        }

        //create a file to write bitmap data
        imageFile= new File(directory.getAbsolutePath(), fileName);
        if(!imageFile.exists()){
            imageFile.createNewFile();
        }

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = imageFile.getAbsolutePath();

        return imageFile;
    }
}
