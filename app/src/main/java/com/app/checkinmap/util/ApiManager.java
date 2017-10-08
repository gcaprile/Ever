package com.app.checkinmap.util;

import android.app.Activity;
import android.content.Context;

import com.salesforce.androidsdk.rest.ApiVersionStrings;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by ASUS-PC on 02/10/2017.
 */

public class ApiManager {

    public interface OnObjectListener{
        void onObject(boolean success, JSONObject jsonObject,String errorMessage);
    }

    public interface OnArrayObjectListener{
        void onArrayObject(boolean success, JSONArray jsonArray, String errorMessage);
    }

    private static ApiManager Instance;

    public static ApiManager getInstance(){

        if(Instance==null){
            Instance = new ApiManager();
        }
        return Instance;
    }

    /**
     * This method help us to get a single JSONObject
     */
    public void getJSONObject(final Context context, String sql, final OnObjectListener listener ){


        RestRequest restRequest = null;
        try {
            restRequest = RestRequest.getRequestForQuery(ApiVersionStrings.getVersionNumber(context), sql);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Utility.getRestClient().sendAsync(restRequest, new RestClient.AsyncRequestCallback() {
            @Override
            public void onSuccess(RestRequest request, final RestResponse result) {
                result.consumeQuietly(); // consume before going back to main thread
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            listener.onObject(true,result.asJSONObject(),null);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onObject(false,null,e.getMessage());
                        } catch (IOException e) {
                            e.printStackTrace();
                            listener.onObject(false,null,e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onError(final Exception exception) {
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onObject(false,null,exception.toString());
                    }
                });
            }
        });
    }
}


