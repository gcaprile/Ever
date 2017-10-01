package com.app.checkinmap.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.checkinmap.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class CheckInMapActivity extends FragmentActivity implements OnMapReadyCallback,
        View.OnClickListener{

    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=102;
    private GoogleMap                   mMap;
    private static final String         TAG= "CheckInMapActivity";
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean                     mLocationPermissionGranted=false;
    private Location                    mLastKnownLocation;
    private LatLng                      mDefaultLocation = new LatLng(13.701258,-89.224707);
    private int                         DEFAULT_ZOOM=10;
    private LocationManager             mLocationManager;
    private boolean                     mIsCheckIn=true;
    private boolean                     mUpdateLocationLabel=false;

    private LinearLayout                mLnlLocationControls;
    private TextView                    mTxvCheckInLatitude;
    private TextView                    mTxvCheckInLang;
    private TextView                    mTxvCheckOutLatitude;
    private TextView                    mTxvCheckOutLang;
    private Chronometer                 mChronometer;
    private Button                      mBtnCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_map);

        //Here we get the location manager
       mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //Here we  get references to the widgets
        mLnlLocationControls=   findViewById(R.id.linear_layout_controls);
        mTxvCheckInLatitude=    findViewById(R.id.text_view_check_in_latitude);
        mTxvCheckInLang=        findViewById(R.id.text_view_check_in_lang);
        mTxvCheckOutLatitude=   findViewById(R.id.text_view_check_out_latitude);
        mTxvCheckOutLang=       findViewById(R.id.text_view_check_out_lang);
        mChronometer=           findViewById(R.id.chronometer_view);
        mBtnCheck=              findViewById(R.id.button_check);

        initLocationControls();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * This method initialize the location
     * controls in the screen
     */
    public void initLocationControls(){
        mTxvCheckInLatitude.setText("");
        mTxvCheckInLang.setText("");
        mTxvCheckOutLatitude.setText("");
        mTxvCheckOutLang.setText("");
        mChronometer.setText(R.string.initial_time);
        mBtnCheck.setOnClickListener(this);
        mLnlLocationControls.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    updateLocationUI();
                }else{
                    showRationale();
                }
            }
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Here we update the UI in the map
        updateLocationUI();

        //Here we get current device location
        getDeviceLocation();
    }

    /**
     * This method help us to check if the app
     * has the permission to get the current device
     * location
     */
    private void getLocationPermission() {
    /*Request location permission, so that we can get the location of the device*/
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            updateLocationUI();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * This method help us to update the UI
     * when the app has the permission to get
     * the device location
     */
    public void updateLocationUI(){
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                getDeviceLocation();
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * This method make the location request
     * in order to get the las device location
     */
    public void getDeviceLocation(){
/*
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();
                           if(mLastKnownLocation!=null){
                               mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                       new LatLng(mLastKnownLocation.getLatitude(),
                                               mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                               if(mUpdateLocationLabel){
                                   updateLocationLabel(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude());
                               }else{
                                   mLnlLocationControls.setVisibility(View.VISIBLE);
                               }
                           }else{
                              loadDefaultLocation();
                           }
                        } else {
                           loadDefaultLocation();
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * This method load the default location
     * in the map
     */
     public void loadDefaultLocation(){
         Log.d(TAG, "Current location is null. Using defaults.");
         mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
         mMap.getUiSettings().setMyLocationButtonEnabled(false);
         if(mUpdateLocationLabel){
             updateLocationLabel(mDefaultLocation.latitude,mDefaultLocation.longitude);
         }else{
             mLnlLocationControls.setVisibility(View.VISIBLE);
         }
     }

    /**
     * This method update lla the label in location
     * controls
     */
    private void updateLocationLabel(double latitude, double lang){
        if(mIsCheckIn){
            mIsCheckIn=false;
            mBtnCheck.setText(R.string.check_out);
            mTxvCheckInLatitude.setText(String.valueOf(latitude));
            mTxvCheckInLang.setText(String.valueOf(lang));
            mTxvCheckOutLatitude.setText("");
            mTxvCheckOutLang.setText("");
            mChronometer.setBase(SystemClock.elapsedRealtime());
            mChronometer.start();
        }else{
            mIsCheckIn=true;
            mBtnCheck.setText(R.string.check_in);
            mTxvCheckOutLatitude.setText(String.valueOf(latitude));
            mTxvCheckOutLang.setText(String.valueOf(lang));
            mChronometer.stop();
        }
    }


    /**
     * This method help us to show the rationale
     * for the location permission
     */
    public void showRationale(){
       new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setMessage(R.string.location_permission_rationale)
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getLocationPermission();
                    }
                }).setCancelable(false).show();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_check:
                if(isGpsEnable()){
                    mUpdateLocationLabel=true;
                    getDeviceLocation();
                }else{
                    showGpsDisableMessage();
                    if(mIsCheckIn){
                        cleanLocationControls();
                    }
                }
                break;
        }
    }

    /**
     * This method help us to clean all the data
     * in location controls
     */
    public void cleanLocationControls(){
        mTxvCheckInLatitude.setText("");
        mTxvCheckInLang.setText("");
        mTxvCheckOutLatitude.setText("");
        mTxvCheckOutLang.setText("");
        mChronometer.setBase(SystemClock.elapsedRealtime());
    }

    /**
     * This method check if the GPS is enable
     */
    private boolean isGpsEnable(){
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * This method help us to ope the settings in order to enable the
     * GPS in the device
     */
    public void openGpsSettings(){
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }


    /**
     * This method help us to show a message
     * to indicate the user have to enable the GPS
     */
    public void showGpsDisableMessage(){
        new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setMessage(R.string.gps_disable_message)
                .setPositiveButton(R.string.go_to_settings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        openGpsSettings();
                        dialogInterface.dismiss();
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setCancelable(false).show();
    }
}
