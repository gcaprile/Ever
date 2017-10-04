package com.app.checkinmap.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.app.checkinmap.R;
import com.app.checkinmap.service.LocationService;
import com.app.checkinmap.util.PreferenceManager;
import com.app.checkinmap.util.Utility;
import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.salesforce.androidsdk.rest.RestClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.app.checkinmap.ui.activity.MapRouteActivity.PERMISSION_LOCATION_REQUEST;

public class DashBoardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.toolbar)
    Toolbar mToolBar;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.nav_view)
    NavigationView mNavigationView;

    @BindView(R.id.text_view_connection_type)
    TextView mTxvConnectionType;

    @BindView(R.id.button_start_rout)
    TextView mTxvRouteButton;

    private boolean mIsSeller=true;
    private boolean mLocationSettingCalled=false;
    private LocationManager mLocationManager;

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
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        //Here we get the location manager
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mToolBar.setTitle(R.string.emasal);
        setSupportActionBar(mToolBar);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);

        setUserData();
    }

    public void setUserData(){
        /*Here we set the user data*/
        TextView mTxvAccountName = (TextView)mNavigationView.getHeaderView(0).findViewById(R.id.text_view_account_name) ;

        TextView mTxvEmail= (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.text_view_email) ;

        RestClient.ClientInfo ci = Utility.getRestClient().getClientInfo();
        mTxvAccountName.setText(ci.displayName);
        mTxvEmail.setText(ci.email);

        if(isInRoute()){
            mTxvRouteButton.setText(R.string.finalize_route);
            mTxvRouteButton.setBackgroundColor(getResources().getColor(R.color.colorRed));
        }else{
            mTxvRouteButton.setText(R.string.start_route);
            mTxvRouteButton.setBackgroundColor(getResources().getColor(R.color.colorBlue));
        }
        updateUi();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_LOCATION_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                   startRoute();
                } else {
                    showRationale();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_seller:
               mIsSeller=true;
                updateUi();
                return true;
            case R.id.action_technical:
                mIsSeller=false;
                updateUi();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
            startActivity(MyAccountsActivity.getIntent(this));
        } else if (id == R.id.nav_my_orders) {
            startActivity(MyAccountsActivity.getIntent(this));
        }  else if (id == R.id.nav_sync) {

        } else if (id == R.id.nav_exit) {
            SalesforceSDKManager.getInstance().logout(this);
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @OnClick(R.id.button_start_rout)
    public void startRoute(){
        //startActivity(MapRouteActivity.getIntent(this));
       if(isInRoute()){
           stopLocationService();
           mTxvRouteButton.setText(R.string.start_route);
           mTxvRouteButton.setBackgroundColor(getResources().getColor(R.color.colorBlue));
           PreferenceManager.getInstance(this).setIsInRoute(false);
       }else{
           if(checkAndRequestPermissions()){
               if(isGpsEnable()){
                   startLocationService();
                   mTxvRouteButton.setText(R.string.finalize_route);
                   mTxvRouteButton.setBackgroundColor(getResources().getColor(R.color.colorRed));
                   PreferenceManager.getInstance(this).setIsInRoute(true);
               }else{
                   showGpsDisableMessage();
               }
           }
       }
    }

    /**
     * This method help us to update
     * the ui using role
     */
    public void updateUi(){
        if(!PreferenceManager.getInstance(this).isInRoute()){
            PreferenceManager.getInstance(this).setIsSeller(mIsSeller);
            if(mIsSeller){
                mTxvConnectionType.setText(R.string.connected_like_sales_person);
                mNavigationView.getMenu().findItem(R.id.nav_my_orders).setVisible(false);
                mNavigationView.getMenu().findItem(R.id.nav_my_accounts).setVisible(true);
            }else{
                mTxvConnectionType.setText(R.string.connected_like_technical);
                mNavigationView.getMenu().findItem(R.id.nav_my_orders).setVisible(true);
                mNavigationView.getMenu().findItem(R.id.nav_my_accounts).setVisible(false);
            }
        }else{
            Toast.makeText(this,R.string.check_rol_explanation,Toast.LENGTH_LONG).show();
        }
    }

    private  boolean checkAndRequestPermissions() {
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        //int storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),PERMISSION_LOCATION_REQUEST);
            return false;
        }
        return true;
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
}
