package com.app.checkinmap.util;

import android.app.Activity;
import android.content.Context;

import com.app.checkinmap.model.CheckPointLocation;
import com.app.checkinmap.model.Route;
import com.salesforce.androidsdk.rest.ApiVersionStrings;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

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
            return;
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

    /**
     * This method help us to make the upsert
     * in sales force
     */
    public void makeRouteUpsert(final Context context,Route route,final OnObjectListener listener){

        HashMap<String,Object> dataSend = new HashMap<>();
        dataSend.put("Name",route.getName());
        dataSend.put("Hora_Inicio__c",route.getStartDateSalesForceDate());
        dataSend.put("Hora_Fin__c",route.getEndDateSalesForceDate());
        dataSend.put("Kilometraje__c",route.getMileage());
        dataSend.put("User__c",route.getUserId());
        if(Utility.getUserRole() == Utility.Roles.TECHNICAL){
            dataSend.put("RecordTypeId","0126A000000l355QAA");
        }else{
            if(Utility.getUserRole() == Utility.Roles.SELLER){
                dataSend.put("RecordTypeId","0126A000000l34lQAA");
            }
        }


        RestRequest restRequest = null;
        try {
            restRequest = RestRequest.getRequestForUpsert(ApiVersionStrings.getVersionNumber(context), "Ruta__c",
                    "Id", null, dataSend);


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

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * This method help us to make the upsert
     * in sales force
     */
    public void makeVisitUpsert(final Context context,String routeId, CheckPointLocation visit, final OnObjectListener listener){

        HashMap<String,Object> dataSend = new HashMap<>();
        dataSend.put("Cliente_Potencial_Visitado__c",visit.getLeadId());
        dataSend.put("Contacto_Orden_de_Trabajo__c",visit.getWorkOrderContactId());
        dataSend.put("Contacto_Visitado__c",visit.getAccountContactId());
        dataSend.put("Direccion__c",visit.getAddressId());
        dataSend.put("Hora_Inicio__c",visit.getCheckInDateSalesForceDate());
        dataSend.put("Hora_Fin__c",visit.getCheckOutDateSalesForceDate());
        dataSend.put("Horas_de_Visita__c",visit.getVisitTimeNumber());
        dataSend.put("Horas_Tiempo_de_Viaje__c",visit.getTravelTimeNumber());
        dataSend.put("Orden_de_Trabajo__c",visit.getWorkOrderId());
        dataSend.put("Razon_Visita__c",visit.getVisitType());
        dataSend.put("RecordTypeId",visit.getRecordType());
        dataSend.put("Resumen_Visita__c",visit.getDescription());
        dataSend.put("Ruta__c",routeId);
        dataSend.put("Tecnico_Principal__c",visit.getTechnicalId());
        dataSend.put("Name",visit.getName());


        RestRequest restRequest = null;
        try {
            restRequest = RestRequest.getRequestForUpsert(ApiVersionStrings.getVersionNumber(context), "Visita__c",
                    "Id", null, dataSend);
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

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * This method help us to make the upsert
     * in sales force
     */
    public void updateWorkOrderStatus(final Context context,String workOrderId, String status,final OnObjectListener listener){

        HashMap<String,Object> dataSend = new HashMap<>();
        dataSend.put("Status",status);

        RestRequest restRequest = null;
        try {
            restRequest = RestRequest.getRequestForUpdate(ApiVersionStrings.getVersionNumber(context), "WorkOrder",
                    workOrderId, dataSend);

            Utility.getRestClient().sendAsync(restRequest, new RestClient.AsyncRequestCallback() {
                @Override
                public void onSuccess(RestRequest request, final RestResponse result) {
                    result.consumeQuietly(); // consume before going back to main thread
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(result.isSuccess()){
                                listener.onObject(true,null,null);
                            }else{
                                listener.onObject(false,null,null);
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

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method help us to make the update
     * in the address object when the new coordinates
     */
    public void updateAddressCoordinates(final Context context,String objectId,String objectName, HashMap<String,Object> dataSend ,final OnObjectListener listener){



        RestRequest restRequest = null;
        try {
            restRequest = RestRequest.getRequestForUpdate(ApiVersionStrings.getVersionNumber(context), objectName,
                    objectId, dataSend);

            Utility.getRestClient().sendAsync(restRequest, new RestClient.AsyncRequestCallback() {
                @Override
                public void onSuccess(RestRequest request, final RestResponse result) {
                    result.consumeQuietly(); // consume before going back to main thread
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(result.isSuccess()){
                                listener.onObject(true,null,null);
                            }else{
                                listener.onObject(false,null,null);
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

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method help us to send the sing image
     * to sales force object
     */
    public void sendSingToSalesForce(final Context context, String workOrderId, String fileImagePath,String fileName,final OnObjectListener listener){

        HashMap<String,Object> dataSend = new HashMap<>();
        dataSend.put("Name",fileName);
        dataSend.put("ParentId",workOrderId);
        dataSend.put("Body",ImageHelper.getBase64FromImage(fileImagePath));

        RestRequest restRequest = null;
        try {
            //Attachment
            restRequest = RestRequest.getRequestForCreate(ApiVersionStrings.getVersionNumber(context), "Attachment",dataSend);

            Utility.getRestClient().sendAsync(restRequest, new RestClient.AsyncRequestCallback() {
                @Override
                public void onSuccess(RestRequest request, final RestResponse result) {
                    result.consumeQuietly(); // consume before going back to main thread
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(result.isSuccess()){
                                try {
                                    listener.onObject(true,result.asJSONObject(),null);
                                } catch (JSONException e) {
                                    listener.onObject(false,null,e.getMessage());
                                } catch (IOException e) {
                                    listener.onObject(false,null,e.getMessage());
                                }
                            }else{
                                listener.onObject(false,null,null);
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

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


