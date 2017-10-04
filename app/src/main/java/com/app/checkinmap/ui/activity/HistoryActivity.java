package com.app.checkinmap.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;


import com.app.checkinmap.R;
import com.app.checkinmap.db.DatabaseManager;
import com.app.checkinmap.model.UserLocation;
import com.app.checkinmap.ui.adapter.HistoryAdapterList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryActivity extends AppCompatActivity {

    @BindView(R.id.rvHistory)
    RecyclerView mRv;

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
        }


        /*Here we set the route data*/
        mTvDistance.setText(getRoutDistance());
        mTvUsedTime.setText(getRouteTime());
        mTvVisitNumber.setText(String.valueOf(DatabaseManager.getInstance().getCheckPointLocationList().size()));

        mRv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRv.setLayoutManager(layoutManager);
        HistoryAdapterList adapter = new HistoryAdapterList(getLocations());
        mRv.setAdapter(adapter);

    }

    private List<UserLocation> getLocations(){
        return DatabaseManager.getInstance().getUserLocationList();
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
        float distance=0;

        List<UserLocation> userLocations = getLocations();

        for(int i=0;i<userLocations.size();i++){
            if((i+1)<userLocations.size()){
                UserLocation userLocationA = userLocations.get(i);
                UserLocation userLocationB = userLocations.get(i+1);

                Location locationA = new Location("");
                Location locationB = new Location("");

                locationA.setLongitude(userLocationA.getLongitude());
                locationA.setLatitude(userLocationA.getLatitude());

                locationB.setLongitude(userLocationB.getLongitude());
                locationB.setLatitude(userLocationB.getLatitude());

                distance = distance + locationA.distanceTo(locationB);
            }
        }

        routeDistance = String.format("%.2f", (distance/1000))+" Km";
        return routeDistance;
    }

    /**
     * This method help us to get the total time
     * used in the route
     */
    public String getRouteTime(){
        String time ="";

        List<UserLocation> userLocations = getLocations();

        if(userLocations.size()>0){

            String dateStart = userLocations.get(0).getDate();
            String dateStop = userLocations.get(userLocations.size()-1).getDate();

            //HH converts hour in 24 hours format (0-23), day calculation
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            Date d1;
            Date d2;

            try {
                d1 = format.parse(dateStart);
                d2 = format.parse(dateStop);

                //in milliseconds
                long diff = d2.getTime() - d1.getTime();

                long diffSeconds = diff / 1000 % 60;
                long diffMinutes = diff / (60 * 1000) % 60;
                long diffHours = diff / (60 * 60 * 1000) % 24;
                long diffDays = diff / (24 * 60 * 60 * 1000);

                time = diffHours+" horas "+ diffMinutes+" minutos ";

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return time;
    }
}
