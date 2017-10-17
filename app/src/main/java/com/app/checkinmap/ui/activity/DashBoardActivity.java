package com.app.checkinmap.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.app.checkinmap.R;
import com.app.checkinmap.bus.BusProvider;
import com.app.checkinmap.bus.NewLocationEvent;
import com.app.checkinmap.db.DatabaseManager;
import com.app.checkinmap.model.CheckPointLocation;
import com.app.checkinmap.model.Route;
import com.app.checkinmap.model.UserLocation;
import com.app.checkinmap.service.LocationService;
import com.app.checkinmap.util.ApiManager;
import com.app.checkinmap.util.PreferenceManager;
import com.app.checkinmap.util.Utility;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.salesforce.androidsdk.rest.RestClient;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

import static com.app.checkinmap.ui.activity.CheckPointMapActivity.ARG_CHECK_POINT_LOCATION_ID;
import static com.app.checkinmap.ui.activity.CheckPointMapActivity.PERMISSION_LOCATION_REQUEST;
import static com.app.checkinmap.ui.activity.MyAccountsActivity.REQUEST_ACCOUNT_SELECTION;
import static com.app.checkinmap.ui.activity.MyLeadsActivity.REQUEST_LEAD_SELECTION;
import static com.app.checkinmap.ui.activity.MyOrderWorksActivity.REQUEST_WORK_ORDER_SELECTION;

public class DashBoardActivity extends AppCompatActivity
        implements OnMapReadyCallback,NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.toolbar)
    Toolbar mToolBar;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.nav_view)
    NavigationView mNavigationView;

    @BindView(R.id.button_start_rout)
    TextView mTxvRouteButton;

    @BindView(R.id.linear_layout_progress)
    LinearLayout mLnlProgress;


    private boolean                     mLocationSettingCalled=false;
    private LocationManager             mLocationManager;
    private GoogleMap                   mMap;
    private boolean                     mLocationPermissionGranted=false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private MaterialDialog              mMaterialProgressDialog;


    /**
     * This method help us to get a single intent
     * from Main activity
     */
    public static Intent getIntent(Context context){
        Intent intent = new Intent(context,DashBoardActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        ButterKnife.bind(this);

        //Here we get the location manager
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        /*Here we get initialize the map*/
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Here we initialize the toolbar an main menu
        initToolbarAndMenu();

        //Her we update the user data
        setUserDataInMenu();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /*Here we check the permission*/
        if(checkAndRequestPermissions()){
            if(isGpsEnable()){
                /*Here we request a explicit user location request*/
                getUserLocation();
            }else{
                showGpsDisableMessage();
            }
        }
    }

    /**
     * This method initialize the main menu and
     * the tool bar
     */
    public void initToolbarAndMenu(){
        mToolBar.setTitle(R.string.emasal);
        setSupportActionBar(mToolBar);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);
    }


    /**
     * This method help us to load the user data in the
     * lateral main menu
     */
    public void setUserDataInMenu(){
        /*Here we set the user data*/
        TextView mTxvAccountName = mNavigationView.getHeaderView(0).findViewById(R.id.text_view_account_name) ;

        TextView mTxvUserName=  mNavigationView.getHeaderView(0).findViewById(R.id.text_view_user_name) ;

        TextView mTxvProfileName= mNavigationView.getHeaderView(0).findViewById(R.id.text_view_profile_name) ;

        RestClient.ClientInfo ci = Utility.getRestClient().getClientInfo();
        mTxvAccountName.setText(ci.displayName);
        mTxvUserName.setText(ci.username);
        mTxvProfileName.setText(Utility.getUserProfileName());

        /*Here we show or hide the menu options*/
        switch (Utility.getUserRole()){
            case SELLER:
                mNavigationView.getMenu().findItem(R.id.nav_my_accounts).setVisible(true);
                mNavigationView.getMenu().findItem(R.id.nav_candidates).setVisible(true);
                break;
            case TECHNICAL:
                mNavigationView.getMenu().findItem(R.id.nav_my_orders).setVisible(true);
                break;
            default:
                mTxvRouteButton.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
        if(mLocationSettingCalled){
            mLocationSettingCalled=false;
            if(isGpsEnable()){
                getUserLocation();
            }else{
                showGpsDisableMessage();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!isInRoute()){
            stopLocationService();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_LOCATION_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted=true;
                  getUserLocation();
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
            case REQUEST_ACCOUNT_SELECTION:
            case REQUEST_LEAD_SELECTION:
            case REQUEST_WORK_ORDER_SELECTION:
                if(resultCode == RESULT_OK){
                   // startActivity(HistoryActivity.getIntent(this));
                    long checkPointLocationId = data.getExtras().getLong(ARG_CHECK_POINT_LOCATION_ID);

                    showSummaryVisitDialog(checkPointLocationId);

                    //Toast.makeText(this,"Mostrar info para: "+checkPointLocationId,Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_information) {

            startActivity(MyInformationActivity.getIntent(this));

        } else if (id == R.id.nav_my_accounts) {

            startActivityForResult(MyAccountsActivity.getIntent(this), REQUEST_ACCOUNT_SELECTION);

        } else if (id == R.id.nav_candidates) {

            startActivityForResult(MyLeadsActivity.getIntent(this), REQUEST_LEAD_SELECTION);

        }else if (id == R.id.nav_my_orders) {

            startActivityForResult(MyOrderWorksActivity.getIntent(this),REQUEST_WORK_ORDER_SELECTION);

        }  else if (id == R.id.nav_sync) {

        } else if (id == R.id.nav_exit) {

            closeSession();
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @OnClick(R.id.button_start_rout)
    public void startRoute(){
       if(isInRoute()){
           updateRouteDate();
       }else{
           if(checkAndRequestPermissions()){
               if(isGpsEnable()){
                   createRoute();
               }else{
                   showGpsDisableMessage();
               }
           }
       }
    }

    /**
     * This method help us to create the route instance
     */
    public void createRoute(){

        /*Here we update the ui*/
        mTxvRouteButton.setVisibility(View.GONE);
        mLnlProgress.setVisibility(View.VISIBLE);

        String osql = "SELECT Radio_Check_In__c FROM Aplicacion_Movil_EMASAL__c WHERE name = 'config'";

        ApiManager.getInstance().getJSONObject(this, osql, new ApiManager.OnObjectListener() {
            @Override
            public void onObject(boolean success, JSONObject jsonObject, String errorMessage) {
                mLnlProgress.setVisibility(View.GONE);
                if(success){
                    try {
                        final int radius = jsonObject.getJSONArray("records").getJSONObject(0).getInt("Radio_Check_In__c");
                        Realm realm = null;
                        try{
                            realm = Realm.getDefaultInstance();
                            realm.executeTransaction(new Realm.Transaction(){
                                @Override
                                public void execute(Realm realm) {

                                    Route route = new Route();
                                    route.setId(System.currentTimeMillis());

                                    /**Here we create the route name*/
                                    String routeTypeString ="";
                                    if(Utility.getUserRole() == Utility.Roles.SELLER){
                                        routeTypeString = "venta";
                                    }else{
                                        routeTypeString = "tecnica";
                                    }
                                    String name = Utility.getDateForName()+"-"+Utility.getRestClient().getClientInfo().displayName+"-"+routeTypeString+
                                            "-"+ DatabaseManager.getInstance().getCorrelativeRoute(Utility.getDateForSearch());
                                    route.setName(name);
                                    route.setStartDate(Utility.getCurrentDate());
                                    route.setUserId(Utility.getRestClient().getClientInfo().userId);
                                    route.setTypeId(Utility.getUserProfileId());

                                    realm.copyToRealmOrUpdate(route);

                                    PreferenceManager.getInstance(getApplicationContext()).setIsInRoute(true);
                                    PreferenceManager.getInstance(getApplicationContext()).setRadius(radius);
                                    PreferenceManager.getInstance(getApplicationContext()).setRouteId(route.getId());
                                    mTxvRouteButton.setVisibility(View.VISIBLE);
                                    updateButtonUi();
                                    Log.d("REALM", "ROUTE SUCCESS");
                                    Log.d("ROUTE NAME", name);
                                }
                            });
                        }catch(Exception e){
                            Log.d("REALM ERROR", e.toString());
                            mTxvRouteButton.setVisibility(View.VISIBLE);
                            updateButtonUi();
                            showMessage(R.string.text_no_route_created);
                        }finally {
                            if(realm != null){
                                realm.close();
                            }
                        }
                    } catch (JSONException e) {
                        //e.printStackTrace();
                        mTxvRouteButton.setVisibility(View.VISIBLE);
                        updateButtonUi();
                        showMessage(R.string.text_no_radius);
                    }
                }else{
                    mTxvRouteButton.setVisibility(View.VISIBLE);
                    updateButtonUi();
                    showMessage(R.string.text_no_radius);
                }
            }
        });
    }

    /**
     * This method help use to show a message
     */
    public void showMessage(int message){
        new MaterialDialog.Builder(this)
                .title(R.string.app_name)
                .content(message)
                .positiveColorRes(R.color.colorPrimary)
                .positiveText(R.string.accept)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .cancelable(false)
                .show();
    }

    /**
     * This method check if the app has all the
     * permissions needed
     */
    private  boolean checkAndRequestPermissions() {
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),PERMISSION_LOCATION_REQUEST);
            mLocationPermissionGranted=false;
            return false;
        }
        mLocationPermissionGranted=true;
        return true;
    }

    /**
     * This method help us to show the rationale
     * for the location permission
     */
    public void showRationale(){
        new MaterialDialog.Builder(this)
                .title(R.string.app_name)
                .content(R.string.location_permission_rationale)
                .positiveColorRes(R.color.colorPrimary)
                .positiveText(R.string.accept)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        checkAndRequestPermissions();
                    }
                })
                .cancelable(false)
                .show();
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


    /**
     * This method check if the user is in route
     * mode
     */
    private boolean isInRoute(){
        return PreferenceManager.getInstance(this).isInRoute();
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

        new MaterialDialog.Builder(this)
                .title(R.string.app_name)
                .content(R.string.gps_disable_message)
                .positiveColorRes(R.color.colorPrimary)
                .positiveText(R.string.text_enable)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mLocationSettingCalled=true;
                        openGpsSettings();
                        dialog.dismiss();
                    }
                })
                .cancelable(false)
                .show();
    }


    @Subscribe
    public void getNewLocation(NewLocationEvent newLocationEvent){
        Log.d("LOCATION LAT:" , String.valueOf(newLocationEvent.getLat()));
        Log.d("LOCATION LON:" , String.valueOf(newLocationEvent.getLon()));

        updateUserLocation(newLocationEvent.getLat(),newLocationEvent.getLon());
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
                                updateUserLocation(location.getLatitude(),location.getLongitude());
                            }
                        }
                        /*Here we start the location service*/
                        startLocationService();

                            /*Here we update the button ui*/
                        updateButtonUi();
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * This method help us to update the user location
     */
      public void updateUserLocation(double latitude, double longitude){
          if (mMap != null) {

            /*Here we clean the old marker*/
              mMap.clear();

            /*Here we add the new one*/
              mMap.addMarker(
                      new MarkerOptions()
                              .position(new LatLng(latitude, longitude))
                              .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

              mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                      new LatLng(latitude,
                              longitude), 17));

              if(mLnlProgress.isShown()){
                  mLnlProgress.setVisibility(View.GONE);
              }

              if(!mTxvRouteButton.isShown()){
                  mTxvRouteButton.setVisibility(View.VISIBLE);
              }
          }
      }

      /**
       * This method help us to update the
       * UI main button
       */
      public void updateButtonUi(){
          /*Here we check if we are in route*/
          if(isInRoute()){
              mTxvRouteButton.setText(R.string.finalize_route);
              mTxvRouteButton.setBackgroundColor(getResources().getColor(R.color.colorRed));
          }else{
              mTxvRouteButton.setText(R.string.start_route);
              mTxvRouteButton.setBackgroundColor(getResources().getColor(R.color.colorBlue));
          }
      }

       /**
        * This method help us to show a message
        * with an overview about the last visit
       */
       public void  showSummaryVisitDialog(long checkPointLocationId){
           CheckPointLocation checkPointLocation = DatabaseManager.getInstance().getCheckPointLocation(checkPointLocationId);
           TextView  visitTime;
           TextView  visitTypeLabel;
           TextView  visitType;
           TextView  visitTypeDescriptionLabel;
           TextView  visitTypeDescription;
           TextView  contactName;

           MaterialDialog dialog = new MaterialDialog.Builder(this)
                   .titleColorRes(R.color.colorPrimary)
                   .title(R.string.text_visit_overview)
                   .customView(R.layout.dialog_visit_summary,true)
                   .positiveColorRes(R.color.colorPrimary)
                   .positiveText(R.string.accept)
                   .onPositive(new MaterialDialog.SingleButtonCallback() {
                       @Override
                       public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                           dialog.dismiss();
                       }
                   }).cancelable(false)
                   .build();

           /*Here we get reference to the layout widgets*/
           View dialogView = dialog.getCustomView();
           visitTime= dialogView.findViewById(R.id.text_view_visit_time);
           visitTypeLabel= dialogView.findViewById(R.id.text_view_visit_type_label);
           visitType = dialogView.findViewById(R.id.text_view_visit_type);
           visitTypeDescriptionLabel = dialogView.findViewById(R.id.text_view_visit_description_label);
           visitTypeDescription = dialogView.findViewById(R.id.text_view_visit_description);
           contactName = dialogView.findViewById(R.id.text_view_contact);

           switch (checkPointLocation.getRecordType()){
               case "0126A000000l3CuQAI":
                   visitTime.setText(checkPointLocation.getVisitTime());
                   visitType.setText(checkPointLocation.getVisitType());
                   visitTypeDescription.setText(checkPointLocation.getDescription());
                   contactName.setText(checkPointLocation.getAccountContactName());
                   break;
               case "0126A000000l3CzQAI":
                   visitTime.setText(checkPointLocation.getVisitTime());
                   visitType.setText(checkPointLocation.getVisitType());
                   visitTypeDescription.setText(checkPointLocation.getDescription());
                   contactName.setText(checkPointLocation.getAccountContactName());
                   break;
               case "0126A000000l3D4QAI":
                   visitTime.setText(checkPointLocation.getVisitTime());
                   visitType.setVisibility(View.GONE);
                   visitTypeLabel.setVisibility(View.GONE);
                   visitTypeDescriptionLabel.setVisibility(View.GONE);
                   visitTypeDescription.setVisibility(View.GONE);
                   contactName.setText(checkPointLocation.getAccountContactName());
                   break;
           }

           dialog.show();
       }

    /**
     * This method help us to update the  finalize
     * route date and close the route
     */
    public void updateRouteDate() {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    Route routeCopy;
                    Route toEdit = realm.where(Route.class)
                            .equalTo("id", PreferenceManager.getInstance(getApplicationContext()).getRouteId()).findFirst();
                    toEdit.setEndDate(Utility.getCurrentDate());
                    realm.copyToRealmOrUpdate(toEdit);

                    /*Here we send the data to sales force*/
                    routeCopy = realm.copyFromRealm(toEdit);
                    sendToSalesForce(routeCopy);
                    Log.d("REALM"," actualizada");
                }
            });
        }catch (Exception e){
            showMessage(R.string.text_no_route_finalize);
        }finally {
            if(realm!=null){
                realm.close();
            }
        }
    }

    /**
     * This method help us to send the data to sales force
     */
    public void sendToSalesForce(final Route route){
        showProgressDialog();
        ApiManager.getInstance().makeRouteUpsert(this, route, new ApiManager.OnObjectListener() {
            @Override
            public void onObject(boolean success, JSONObject jsonObject, String errorMessage) {
                if(success){
                    try {
                        if(jsonObject.getBoolean("success")){

                            /*Here we get the sales force route id*/
                            String routeId = jsonObject.getString("id");

                            /*Here we get the visits from the database*/
                            List<CheckPointLocation> checkPointLocations = DatabaseManager.getInstance().getCheckPointLocationList(route.getId());

                            /*Here we sen the data to sales force*/
                            sendVisitToSalesForce(checkPointLocations,routeId,0);
                        }else{
                            showMessage(R.string.text_route_no_saved);
                            hideProgressDialog();
                        }
                    } catch (JSONException e) {
                        showMessage(R.string.text_route_no_saved);
                        hideProgressDialog();
                    }
                }else{
                    showMessage(R.string.text_route_no_saved);
                    hideProgressDialog();
                }
            }
        });
    }

    /**
     * Here we show all the data about
     * the route
     */
    public void callHistoryActivity(){
        startActivity(HistoryActivity.getIntent(this));
    }

    /**
     * This method help us to close
     * the current session
     */
    public void closeSession(){
        if(!PreferenceManager.getInstance(this).isInRoute()){
            new MaterialDialog.Builder(this)
                    .title(R.string.app_name)
                    .content(R.string.text_close_session)
                    .positiveColorRes(R.color.colorPrimary)
                    .positiveText(R.string.accept)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            closeSalesForce();
                            dialog.dismiss();
                        }
                    })
                    .negativeColorRes(R.color.colorPrimary)
                    .negativeText(R.string.cancel)
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    })
                    .cancelable(false)
                    .show();
        }else{
            showMessage(R.string.text_close_route_to_finalize);
        }
    }

    /**
     * This method close the sales force
     * session
     */
    public void closeSalesForce(){
        SalesforceSDKManager.getInstance().logout(this);
    }

    /**
     * This method help us to show a single progress dialog
     */
    public void showProgressDialog(){
        mMaterialProgressDialog = new MaterialDialog.Builder(this)
                .title(R.string.app_name)
                .content(R.string.text_sending_route_data)
                .progress(true,0)
                .widgetColor(getResources().getColor(R.color.colorPrimary))
                .cancelable(false)
                .show();

    }

    /**
     * This method help us to hide the progress
     * dialog
     */
    public void hideProgressDialog(){
        mMaterialProgressDialog.dismiss();
    }

    /**
     * This method help us to send all the visit
     * to sales force account
     */
    public void sendVisitToSalesForce(final List<CheckPointLocation> visits, final String routeId, final int position){
        if(position<visits.size()){
            ApiManager.getInstance().makeVisitUpsert(this, routeId, visits.get(position), new ApiManager.OnObjectListener() {
                @Override
                public void onObject(boolean success, JSONObject jsonObject, String errorMessage) {
                    if(success){
                        try {
                            if(jsonObject.getBoolean("success")){

                                if(visits.get(position).isUpdateAddress()){
                                       updateAddressLocation(visits,routeId,position);
                                }else{
                                    sendVisitToSalesForce(visits,routeId,position+1);
                                }
                            }else{
                                showMessage(R.string.text_route_no_saved);
                                hideProgressDialog();
                            }
                        } catch (JSONException e) {
                            showMessage(R.string.text_route_no_saved);
                            hideProgressDialog();
                        }
                    }else{
                        showMessage(R.string.text_route_no_saved);
                        hideProgressDialog();
                    }
                }
            });
        }else{
            PreferenceManager.getInstance(getApplicationContext()).setIsInRoute(false);
            updateButtonUi();
            callHistoryActivity();
            hideProgressDialog();
        }
    }

    /**
     * This method help us to update the address object
     * in sales force
     */
    public void updateAddressLocation(final List<CheckPointLocation> visits, final String routeId, final int position){
        String addressId = visits.get(position).getAddressId();
        double latitude = visits.get(position).getLatitude();
        double longitude = visits.get(position).getLongitude();

        Log.d("addresId",String.valueOf(addressId));
        Log.d("latitude",String.valueOf(latitude));
        Log.d("longitude",String.valueOf(longitude));


        ApiManager.getInstance().updateAddressCoordinates(this, addressId, latitude, longitude, new ApiManager.OnObjectListener() {
            @Override
            public void onObject(boolean success, JSONObject jsonObject, String errorMessage) {
                if(success){
                    sendVisitToSalesForce(visits,routeId,position+1);
                }else{
                    showMessage(R.string.text_route_no_saved);
                    hideProgressDialog();
                }
            }
        });
    }
}
