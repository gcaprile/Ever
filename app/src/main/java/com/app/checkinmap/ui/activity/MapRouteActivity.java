package com.app.checkinmap.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.checkinmap.R;
import com.app.checkinmap.bus.BusProvider;
import com.app.checkinmap.bus.NewLocationEvent;
import com.app.checkinmap.db.DatabaseManager;
import com.app.checkinmap.model.CheckPointLocation;
import com.app.checkinmap.model.UserLocation;
import com.app.checkinmap.service.LocationService;
import com.app.checkinmap.util.PreferenceManager;
import com.app.checkinmap.util.Utility;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

import static android.R.attr.fillColor;
import static android.R.attr.strokeColor;

public class MapRouteActivity extends AppCompatActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback{

    public static final int PERMISSION_LOCATION_REQUEST = 12;
    public static final int SIGNATURE_REQUEST = 15;
    public static String   ARG_SELECTION="selection";

     @BindView(R.id.button_finalize_route)
     Button mBtnStopRoute;

    @BindView(R.id.button_check)
    Button mBtnCheck;

    @BindView(R.id.linear_layout_check_progress)
    LinearLayout mLnlCheckProgress;

    @BindView(R.id.chronometer_view)
    Chronometer mChronometer;

    private GoogleMap mMap;
    private boolean mIsChecking=false;
    private boolean mLocationPermissionGranted=false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private CheckPointLocation mCheckPointLocation;
    private LocationManager mLocationManager;
    private boolean mLocationSettingCalled=false;
    private Circle mCircle;
    private boolean mIsFinalize=false;

    /**
     * This method help us to get a single
     * map instance
     */
    public static Intent getIntent(Context context,String selection){
        Intent intent = new Intent(context,MapRouteActivity.class);
        intent.putExtra(ARG_SELECTION,selection);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_route);

        ButterKnife.bind(this);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle(getIntent().getExtras().getString(ARG_SELECTION));
        }

        //Here we get the location manager
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /*if (checkAndRequestPermissions()) {
            if(isGpsEnable()){
                startLocationService();
            }else{
                showGpsDisableMessage();
            }
        }*/

        checkAndRequestPermissions();

        if(!PreferenceManager.getInstance(this).isSeller()){
            showNoAddressMessage();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        showSavedLocationOnMap();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationService();
    }

    /**
     * This method help us to check if
     * we the permissions
     */
    private  boolean checkAndRequestPermissions() {
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),PERMISSION_LOCATION_REQUEST);
            mLocationPermissionGranted=false;
            return false;
        }
        mLocationPermissionGranted=true;
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_LOCATION_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startLocationService();
                } else {
                    showRationale();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case SIGNATURE_REQUEST:
                if(resultCode==RESULT_OK){
                    showCheckInFinalizeMessage();
                }
                break;
        }
    }

    /**
     * This method help us to start the location
     * service in the app
     */
    private void startLocationService(){
            startService(new Intent(this, LocationService.class));
    }

    /**
     * This method help us to stop the location
     * service in the app
     */
    private void stopLocationService(){
        stopService(new Intent(this, LocationService.class));
    }

    @OnClick(R.id.button_finalize_route)
    public void finalizeRoute(){
        if(!mIsChecking){
            stopLocationService();
            startActivity(HistoryActivity.getIntent(this));
            finish();
        }else{
            Toast.makeText(this,R.string.checking_is_in_progress,Toast.LENGTH_LONG).show();
        }
    }


    /**
     * This method help us to draw in the map
     * all the user locations saved in the
     * local data base
     */
    private void showSavedLocationOnMap(){
        List<UserLocation> locations = DatabaseManager.getInstance().getUserLocationList();

        for(UserLocation location : locations){
            if (mMap != null) {
                mMap.addMarker(
                        new MarkerOptions()
                                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
            }
        }
        /*Her we make zoom in the last saved location*/
        if(locations.size()>0){
            UserLocation lastLocation = locations.get(locations.size()-1);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(lastLocation.getLatitude(),
                            lastLocation.getLongitude()), 15));
        }
    }

    /**
     * This method help us to get the
     * flg to indicate in the location
     * services is running or not
     */
    private boolean isLocationServiceEnabled(){
        return PreferenceManager.getInstance(this).isServiceEnabled();
    }

    @OnClick(R.id.button_check)
    public void checkUserLocation(){
        if(mIsChecking){
            //showCheckInFinalizeMessage();
            startActivityForResult(SignatureActivity.getIntent(this,getIntent().getExtras().getString(ARG_SELECTION)),SIGNATURE_REQUEST);
        }else{
            mIsChecking = true;
            mBtnCheck.setText(R.string.finalize);
            mLnlCheckProgress.setVisibility(View.VISIBLE);
            mChronometer.setBase(SystemClock.elapsedRealtime());
            mChronometer.start();
            getUserLocation();
            stopLocationService();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
        if(mLocationSettingCalled){
            mLocationSettingCalled=false;
            if(isGpsEnable()){
                startLocationService();
            }else{
                showGpsDisableMessage();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void getNewLocation(NewLocationEvent newLocationEvent){
        Log.d("LOCATION LAT:" , String.valueOf(newLocationEvent.getLat()));
        Log.d("LOCATION LON:" , String.valueOf(newLocationEvent.getLon()));

        if (mMap != null) {
            if(mCircle!=null){
                mCircle.remove();
            }
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(new LatLng(newLocationEvent.getLat(),newLocationEvent.getLon()));
            circleOptions.radius(100);
            circleOptions.fillColor(R.color.colorBlackTransparent);
            circleOptions.strokeColor(R.color.colorBlue);
            circleOptions.strokeWidth(4.0f);
            mCircle = mMap.addCircle(circleOptions);
            mMap.addMarker(
                    new MarkerOptions()
                            .position(new LatLng(newLocationEvent.getLat(), newLocationEvent.getLon()))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(newLocationEvent.getLat(), newLocationEvent.getLon()), 17));
        }
    }

    /**
     * This method make the location request
     * in order to get the las device location
     */
    public void getUserLocation(){
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
                            Location location = (Location) task.getResult();
                            if(location!=null){
                                if(mIsChecking){
                                    mCheckPointLocation = new CheckPointLocation();
                                    mCheckPointLocation.setId(System.currentTimeMillis());
                                    mCheckPointLocation.setCheckInLatitude(location.getLatitude());
                                    mCheckPointLocation.setCheckInLongitude(location.getLongitude());
                                    mCheckPointLocation.setCheckInDate(Utility.getCurrentDate());
                                }else{
                                    mCheckPointLocation.setCheckOutLatitude(location.getLatitude());
                                    mCheckPointLocation.setCheckOutLongitude(location.getLongitude());
                                    mCheckPointLocation.setCheckOutDate(Utility.getCurrentDate());
                                    saveCheckPointLocation();
                                    startActivity(HistoryActivity.getIntent(getApplicationContext()));
                                    finish();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(),R.string.no_user_location_available,Toast.LENGTH_LONG).show();
                                restartCheckInUi();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),R.string.no_user_location_available,Toast.LENGTH_LONG).show();
                            restartCheckInUi();
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * This method help us to restart the
     * check in UI
     */
    public void restartCheckInUi(){
        mBtnCheck.setText(R.string.check_in);
        mChronometer.stop();
        mLnlCheckProgress.setVisibility(View.GONE);
    }


    private void saveCheckPointLocation(){
        Realm realm = null;
        try{
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction(){
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(mCheckPointLocation);
                    Log.d("REALM CHECK POINT", "SUCCESS CHECK");
                }
            });
        }catch(Exception e){
            Log.d("REALM CHECK POINT", e.toString());
        }finally {
            if(realm != null){
                realm.close();
            }
        }
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
                        mLocationSettingCalled=true;
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
                       checkAndRequestPermissions();
                    }
                }).setCancelable(false).show();

    }

    /**
     * This method help us to show a message
     * to indicate the check in is going to
     * finalize
     */
    public void showCheckInFinalizeMessage(){
        new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setMessage(R.string.check_in_finalize)
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finalizeCheckIn();
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setCancelable(false).show();
    }

    /**
     * This method finalize the check in
     * action
     */
    public void finalizeCheckIn(){
        mIsChecking = false;
        mBtnCheck.setText(R.string.check_in);
        mLnlCheckProgress.setVisibility(View.GONE);
        mChronometer.stop();
        getUserLocation();
    }

    /**
     * This method show a message explaining
     * that the selected account doesnt have
     * a location in the data base
     */
    public void showNoAddressMessage(){
        new AlertDialog.Builder(this)
                .setTitle(R.string.no_address_title)
                .setMessage(R.string.no_address_description)
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       dialogInterface.dismiss();
                    }
                }).setCancelable(false).show();
    }
}
