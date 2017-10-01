package com.app.checkinmap.service;


import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.app.checkinmap.R;
import com.app.checkinmap.bus.BusProvider;
import com.app.checkinmap.bus.NewLocationEvent;
import com.app.checkinmap.model.UserLocation;
import com.app.checkinmap.util.Utility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import io.realm.Realm;

public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private NewLocationEvent mNewLocationEventBus;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (mGoogleApiClient == null) {
            configureGooglePlayServices();
        }

        if(mNewLocationEventBus == null){
            mNewLocationEventBus = new NewLocationEvent();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void configureGooglePlayServices() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();
    }

    LocationRequest mLocationRequest;

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(30000);
        mLocationRequest.setFastestInterval(30000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        startLocationUpdates();
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest,
                this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        createLocationRequest();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, R.string.connection_failed_play_services_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, LocationService.this);
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mNewLocationEventBus.setLat(location.getLatitude());
        mNewLocationEventBus.setLon(location.getLongitude());
        saveLocation(location);
        BusProvider.getInstance().post(mNewLocationEventBus);
    }

    private void saveLocation(final Location location){
        Realm realm = null;

        try{
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction(){
                @Override
                public void execute(Realm realm) {
                    UserLocation userLocation = new UserLocation();
                    userLocation.setId(System.currentTimeMillis());
                    userLocation.setLatitude(location.getLatitude());
                    userLocation.setLongitude(location.getLongitude());
                    userLocation.setDate(Utility.getCurrentDate());
                    realm.copyToRealmOrUpdate(userLocation);
                    Log.d("REALM", "SUCCESS");
                }
            });
        }catch(Exception e){
            Log.d("REALM ERROR", e.toString());
        }finally {
            if(realm != null){
                realm.close();
            }
        }
    }
}
