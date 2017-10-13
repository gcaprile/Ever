package com.app.checkinmap.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.app.checkinmap.R;
import com.app.checkinmap.model.CheckPointData;
import com.app.checkinmap.model.Lead;
import com.app.checkinmap.ui.adapter.LeadAdapterList;
import com.app.checkinmap.util.ApiManager;
import com.app.checkinmap.util.PreferenceManager;
import com.app.checkinmap.util.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.app.checkinmap.ui.activity.CheckPointMapActivity.REQUEST_CHECK_IN;
import static com.app.checkinmap.ui.activity.SearchableLeadsActivity.ACTION_SEARCH_RESULT;

public class MyLeadsActivity extends AppCompatActivity implements LeadAdapterList.OnItemClickListener{

    public static final int REQUEST_LEAD_SELECTION = 27;
    public static final String ARG_CHECK_POINT_DATA="lead_selected";

    @BindView(R.id.rcv_leads)
    RecyclerView mRv;

    @BindView(R.id.progress_bar)
    ProgressBar mPgBar;

    @BindView(R.id.text_view_message)
    TextView mTxvMessage;

    private LeadAdapterList mAdapter;

    BroadcastReceiver mSearchResultReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_SEARCH_RESULT)) {
                CheckPointData checkPointData = intent.getExtras().getParcelable(ARG_CHECK_POINT_DATA);
                if(checkPointData!=null){
                    startCheckPointFlow(checkPointData);
                }
            }

        }
    };


    /**
     * This method help us to get a single
     * intent in order to get a my lead activity
     * instance
     */
    public static Intent getIntent(Context context){
        Intent intent = new Intent(context,MyLeadsActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_leads);

        ButterKnife.bind(this);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.candidates);
        }

        mRv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRv.setLayoutManager(layoutManager);

        /*Here we get the leads from the sales force*/
        getLeadFromSalesForce();

        registerReceiver(mSearchResultReceiver, new IntentFilter(SearchableLeadsActivity.ACTION_SEARCH_RESULT));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CHECK_IN:
                if(resultCode ==  RESULT_OK){
                    setResult(RESULT_OK,data);
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mSearchResultReceiver);
    }

    /**
     * This method help us to get all the accounts from
     * sales force
     */
    public void getLeadFromSalesForce(){
        String osql = "SELECT Id, Name, Company, Street, City, State, PostalCode, Country, Pais__c, Latitude, Longitude" +
                " FROM Lead WHERE Pais__c = '"+Utility.getUserCountry()+"'";

        ApiManager.getInstance().getJSONObject(this, osql, new ApiManager.OnObjectListener() {
            @Override
            public void onObject(boolean success, JSONObject jsonObject, String errorMessage) {
                /*Here we hide the progress bar*/
                mPgBar.setVisibility(View.GONE);
                if(success){
                    Utility.logLargeString(jsonObject.toString());
                   try {
                        Type listType = new TypeToken<List<Lead>>() {}.getType();
                        List<Lead> leadList = new Gson().fromJson(jsonObject.getJSONArray("records").toString(), listType);
                        loadListData(leadList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        mTxvMessage.setText(e.getMessage());
                        mTxvMessage.setVisibility(View.VISIBLE);
                    }
                }else{
                    mTxvMessage.setText(errorMessage);
                    mTxvMessage.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_search:
                onSearchRequested();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * This method help us to load the data in the
     * recycler view
     */
    public void loadListData(List<Lead> leadList){

        if(leadList.size()>0){
            mAdapter = new LeadAdapterList(leadList);
            mAdapter.setOnItemClickListener(this);
            mRv.setAdapter(mAdapter);
            mRv.setVisibility(View.VISIBLE);
        }else{
            mTxvMessage.setText(R.string.no_leads_to_show);
            mTxvMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(Lead lead) {
        if(PreferenceManager.getInstance(this).isInRoute()){

            //Here we create the address object
            CheckPointData checkPointData = new CheckPointData();
            checkPointData.setId(lead.getId());
            checkPointData.setLatitude(lead.getLatitude());
            checkPointData.setLongitude(lead.getLongitude());
            checkPointData.setName(lead.getName());
            checkPointData.setCheckPointType(2);
            if(lead.getAddress()!=null){
                checkPointData.setAddress(lead.getAddress());
            }else{
                checkPointData.setAddress("");
            }

            if(lead.getAddress()!=null){
                if(!lead.getAddress().isEmpty()){
                    //Here we start the check flow
                    startActivityForResult(CheckPointMapActivity.getIntent(getApplicationContext(),checkPointData),
                            REQUEST_CHECK_IN);
                }else{
                    showMessage(R.string.no_address_in_this_lead);
                }
            }else{
                showMessage(R.string.no_address_in_this_lead);
            }
        }else{
           showMessage(R.string.you_should_start_the_route);
        }
    }

    /**
     * Here we start the check point flow
     */
    public void startCheckPointFlow(CheckPointData checkPointData){


        startActivityForResult(CheckPointMapActivity.getIntent(getApplicationContext(),checkPointData),
                REQUEST_CHECK_IN);
    }

    /**
     * This method show a single message
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
