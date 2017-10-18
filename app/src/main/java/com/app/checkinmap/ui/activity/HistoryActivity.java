package com.app.checkinmap.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;


import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.app.checkinmap.R;
import com.app.checkinmap.db.DatabaseManager;
import com.app.checkinmap.model.CheckPointLocation;
import com.app.checkinmap.model.Route;
import com.app.checkinmap.model.UserLocation;
import com.app.checkinmap.ui.adapter.HistoryAdapterList;
import com.app.checkinmap.util.PreferenceManager;
import com.app.checkinmap.util.Utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryActivity extends AppCompatActivity {

    @BindView(R.id.rvHistory)
    RecyclerView mRv;

    @BindView(R.id.text_view_title_screen)
    TextView mTxvTitle;

    @BindView(R.id.text_route_name)
    TextView mTvRouteName;

    @BindView(R.id.text_view_distance)
    TextView mTvDistance;

    @BindView(R.id.text_view_time)
    TextView mTvUsedTime;

    @BindView(R.id.text_view_visit_number)
    TextView mTvVisitNumber;


    /**
     * This method help us to get a single
     * intent in order to get a history activity
     * instance
     */
    public static Intent getIntent(Context context){
        Intent intent = new Intent(context,HistoryActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.completed_route);
            mTxvTitle.setText(R.string.route_complete_information);
        }


        /*Here we set the route data*/
        mTvRouteName.setText(DatabaseManager.getInstance().getRouteName(PreferenceManager.getInstance(this).getRouteId()));
        mTvDistance.setText(getRoutDistance());
        mTvUsedTime.setText(getRouteTime());
        mTvVisitNumber.setText(String.valueOf(DatabaseManager.getInstance().getCheckPointLocationList(
                PreferenceManager.getInstance(this).getRouteId()
        ).size()));

        //mRv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRv.setLayoutManager(layoutManager);
        HistoryAdapterList adapter = new HistoryAdapterList(getCheckPointLocations());
        mRv.setAdapter(adapter);

        /*Here we show a success message*/
        showMessage(R.string.text_route_data_sent);

    }

    private List<CheckPointLocation> getCheckPointLocations(){
        return DatabaseManager.getInstance().getCheckPointLocationList(PreferenceManager.getInstance(this).getRouteId());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method help su to calculate
     * the route distance
     */
    public String getRoutDistance(){
        String routeDistance="";
        double distance=0.00;

        List<UserLocation> userLocations = DatabaseManager.getInstance().getUserLocationList(PreferenceManager.getInstance(this).getRouteId());
        List<CheckPointLocation> checkPointLocations = getCheckPointLocations();

        if(userLocations.size()>0){

            Location locationRouteStart = new Location("");
            locationRouteStart.setLongitude(userLocations.get(0).getLongitude());
            locationRouteStart.setLatitude(userLocations.get(0).getLatitude());

            if(checkPointLocations.size()>0){

                Location locationRouteFirstPoint = new Location("");
                locationRouteFirstPoint.setLongitude(checkPointLocations.get(0).getCheckInLongitude());
                locationRouteFirstPoint.setLatitude(checkPointLocations.get(0).getCheckInLatitude());

                 /*First distance in the route*/
                distance = distance + locationRouteStart.distanceTo(locationRouteFirstPoint);
            }
        }

        for(int i=0;i<checkPointLocations.size();i++){
            if((i+1)<checkPointLocations.size()){
                CheckPointLocation userLocationA = checkPointLocations.get(i);
                CheckPointLocation userLocationB = checkPointLocations.get(i+1);

                Location locationA = new Location("");
                Location locationB = new Location("");

                locationA.setLongitude(userLocationA.getCheckInLongitude());
                locationA.setLatitude(userLocationA.getCheckInLatitude());

                locationB.setLongitude(userLocationB.getCheckInLongitude());
                locationB.setLatitude(userLocationB.getCheckInLatitude());

                distance = distance + locationA.distanceTo(locationB);
            }
        }

        routeDistance = String.format("%.2f", (distance/1000.00))+" Km";
        return routeDistance;
    }

    /**
     * This method help us to get the total time
     * used in the route
     */
    public String getRouteTime(){
        String time ="";

        List<CheckPointLocation> userLocations = getCheckPointLocations();

        if(userLocations.size()>0){

            String dateStart = userLocations.get(0).getCheckInDate();
            String dateStop = userLocations.get(userLocations.size()-1).getCheckOutDate();

            //HH converts hour in 24 hours format (0-23), day calculation
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            Date d1;
            Date d2;

            try {
                d1 = format.parse(dateStart);
                d2 = format.parse(dateStop);

                //diff check ins
                long diffCheckIns = d2.getTime() - d1.getTime();

                long diffCheckRoute = 0;

                /*Here we get all the user locations*/
                List<UserLocation> userLocationsMap = DatabaseManager.getInstance().getUserLocationList(PreferenceManager.getInstance(this).getRouteId());

                if(userLocationsMap.size()>0){
                    String dateStartRoute = userLocationsMap.get(0).getDate();
                    String dateStopRoute = userLocationsMap.get(userLocations.size()-1).getDate();

                    Date d3 = format.parse(dateStartRoute);
                    Date d4 = format.parse(dateStopRoute);

                    //diff locations
                   diffCheckRoute = d4.getTime() - d3.getTime();
                }

                long totalDiff = diffCheckIns + diffCheckRoute;

                long diffSeconds = totalDiff / 1000 % 60;
                long diffMinutes = totalDiff / (60 * 1000) % 60;
                long diffHours = totalDiff / (60 * 60 * 1000) % 24;
                long diffDays = totalDiff / (24 * 60 * 60 * 1000);

                time = diffHours+" horas "+ diffMinutes+" minutos ";

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return time;
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
}
