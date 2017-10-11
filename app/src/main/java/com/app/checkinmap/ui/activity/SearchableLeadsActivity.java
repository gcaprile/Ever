package com.app.checkinmap.ui.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.app.checkinmap.ui.activity.CheckPointMapActivity.REQUEST_CHECK_IN;

public class SearchableLeadsActivity extends AppCompatActivity implements LeadAdapterList.OnItemClickListener{

    public static final int REQUEST_LEAD_SELECTION = 27;
    public static final String ACTION_SEARCH_RESULT= "com.app.checkinmap.SEARCH_RESULT";

    @BindView(R.id.rcv_leads)
    RecyclerView mRv;

    @BindView(R.id.progress_bar)
    ProgressBar mPgBar;

    @BindView(R.id.text_view_message)
    TextView mTxvMessage;

    private LeadAdapterList mAdapter;
    private String mQuery;

    /**
     * This method help us to get a single
     * intent in order to get a my lead activity
     * instance
     */
    public static Intent getIntent(Context context){
        Intent intent = new Intent(context,SearchableLeadsActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_leads);

        ButterKnife.bind(this);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.text_result);
        }


        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            mQuery = intent.getStringExtra(SearchManager.QUERY);

            mRv.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mRv.setLayoutManager(layoutManager);

            /*Here we get the leads from the sales force*/
            getLeadFromSalesForce();

        }else{
            mPgBar.setVisibility(View.GONE);
            mTxvMessage.setText(R.string.text_no_result);
            mTxvMessage.setVisibility(View.VISIBLE);
        }

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


    /**
     * This method help us to get all the accounts from
     * sales force
     */
    public void getLeadFromSalesForce(){
        String osql = "SELECT Id, Name, Company, Pais__c, Latitude, Longitude," +
                "Phone, Website, Email, Description FROM Lead WHERE Pais__c = '"+Utility.getUserCountry()+"'";

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
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * This method help us to load the data in the
     * recycler view
     */
    public void loadListData(List<Lead> leadList){

        if(leadList.size()>0){

            List<Lead> list = getFilterLeads(leadList);

            if(list.size()>0){
                mAdapter = new LeadAdapterList(list);
                mAdapter.setOnItemClickListener(this);
                mRv.setAdapter(mAdapter);
                mRv.setVisibility(View.VISIBLE);
            }else{
                mTxvMessage.setText(R.string.text_no_result);
                mTxvMessage.setVisibility(View.VISIBLE);
            }
        }else{
            mTxvMessage.setText(R.string.text_no_result);
            mTxvMessage.setVisibility(View.VISIBLE);
        }
    }

    /**
     * This method help us to filter all the leads
     * in the list
     */
    public List<Lead> getFilterLeads(List<Lead> leadList){
        List<Lead> list = new ArrayList<>();
        for (Lead lead : leadList){
            if(lead.getName()!=null){
                if(lead.getName().toLowerCase().contains(mQuery.toLowerCase())){
                    list.add(lead);
                }
            }else{
                if(lead.getCompany()!=null){
                    if(lead.getCompany().toLowerCase().contains(mQuery.toLowerCase())){
                        list.add(lead);
                    }
                }else{
                    if(lead.getAddress()!=null){
                        if(lead.getAddress().toLowerCase().contains(mQuery.toLowerCase())){
                            list.add(lead);
                        }
                    }
                }
            }
        }

        return list;
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

             /*Here we notify the result*/
            Intent intent= new Intent();
            intent.setAction(ACTION_SEARCH_RESULT);
            intent.putExtra(MyLeadsActivity.ARG_CHECK_POINT_DATA,checkPointData);
            sendBroadcast(intent);

            finish();
        }else{

            new MaterialDialog.Builder(this)
                    .title(R.string.app_name)
                    .content(R.string.you_should_start_the_route)
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
}
