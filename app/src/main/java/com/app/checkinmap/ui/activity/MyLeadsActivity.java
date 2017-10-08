package com.app.checkinmap.ui.activity;

import android.content.Context;
import android.content.Intent;
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

import com.app.checkinmap.R;
import com.app.checkinmap.model.Account;
import com.app.checkinmap.model.Lead;
import com.app.checkinmap.ui.adapter.AccountAdapterList;
import com.app.checkinmap.ui.adapter.LeadAdapterList;
import com.app.checkinmap.util.ApiManager;
import com.app.checkinmap.util.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.app.checkinmap.ui.activity.AccountDetailActivity.REQUEST_ADDRESS_SELECTION;

public class MyLeadsActivity extends AppCompatActivity implements LeadAdapterList.OnItemClickListener{

    public static final int REQUEST_LEAD_SELECTION = 27;

    @BindView(R.id.rcv_leads)
    RecyclerView mRv;

    @BindView(R.id.progress_bar)
    ProgressBar mPgBar;

    @BindView(R.id.text_view_message)
    TextView mTxvMessage;

    private LeadAdapterList mAdapter;

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 22:
                if(resultCode == RESULT_OK){
                    setResult(RESULT_OK);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        return true;
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

    }
}
