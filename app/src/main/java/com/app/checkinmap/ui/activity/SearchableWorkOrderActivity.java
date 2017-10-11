package com.app.checkinmap.ui.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.app.checkinmap.R;
import com.app.checkinmap.model.AccountAddress;
import com.app.checkinmap.model.CheckPointData;
import com.app.checkinmap.model.WorkOrder;
import com.app.checkinmap.ui.adapter.WorkOrderAdapterList;
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

public class SearchableWorkOrderActivity extends AppCompatActivity implements WorkOrderAdapterList.OnItemClickListener{
    public static final String ARG_WORK_ORDERS = "work_orders";
    public static final String ACTION_SEARCH_RESULT= "com.app.checkinmap.SEARCH_RESULT";

    @BindView(R.id.rcv_work_orders)
    RecyclerView mRv;

    @BindView(R.id.progress_bar)
    ProgressBar mPgBar;

    @BindView(R.id.text_view_message)
    TextView mTxvMessage;

    private WorkOrderAdapterList mAdapter;
    private String mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable_work_order);

        ButterKnife.bind(this);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.text_result);
        }

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
           mQuery = intent.getStringExtra(SearchManager.QUERY);

            //Toast.makeText(this,mQuery,Toast.LENGTH_LONG).show();

            mRv.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mRv.setLayoutManager(layoutManager);

            getWorkOrdersFromSalesForce();
        }else{
            mPgBar.setVisibility(View.GONE);
            mTxvMessage.setText(R.string.text_no_result);
            mTxvMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * This method help us to get all the accounts from
     * sales force
     */
    public void getWorkOrdersFromSalesForce(){

        String osql = "SELECT Id, WorkOrderNumber, AccountId, ContactId, Country, " +
                "Latitude, Longitude, Description, StartDate,EndDate, Status FROM WorkOrder";

        ApiManager.getInstance().getJSONObject(this, osql, new ApiManager.OnObjectListener() {
            @Override
            public void onObject(boolean success, JSONObject jsonObject, String errorMessage) {
                /*Here we hide the progress bar*/
                mPgBar.setVisibility(View.GONE);
                if(success){
                    Utility.logLargeString(jsonObject.toString());
                    try {
                        Type listType = new TypeToken<List<WorkOrder>>() {}.getType();
                        List<WorkOrder> workOrderList = new Gson().fromJson(jsonObject.getJSONArray("records").toString(), listType);
                        loadListData(workOrderList);

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

    /**
     * This method help us to load the data in the
     * recycler view
     */
    public void loadListData(List<WorkOrder> workOrderList){

        if(workOrderList.size()>0){

            List<WorkOrder> list = getOrderWorksFiltered(workOrderList);

            if(list.size()>0){
                mAdapter = new WorkOrderAdapterList(list);
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

    public List<WorkOrder>  getOrderWorksFiltered(List<WorkOrder> list){
        List<WorkOrder> workOrderList = new ArrayList<>();

        for (WorkOrder workOrder: list){
            if(workOrder.getWorkOrderNumber().toLowerCase().contains(mQuery.toLowerCase())){
                workOrderList.add(workOrder);
            }else{
                if(workOrder.getAccountName()!=null){
                    if(workOrder.getAccountName().toLowerCase().contains(mQuery.toLowerCase())){
                        workOrderList.add(workOrder);
                    }
                }else{
                    if(workOrder.getContactName()!=null){
                        if(workOrder.getContactName().toLowerCase().contains(mQuery.toLowerCase())){
                            workOrderList.add(workOrder);
                        }
                    }
                }
            }
        }

        return workOrderList;
    }


    @Override
    public void onItemClick(WorkOrder workOrder) {
        if(PreferenceManager.getInstance(this).isInRoute()){
            checkIfIsMainTechnical(workOrder);
        }else{
            showMessage(R.string.you_should_start_the_route);
        }
    }

    /**
     * This method help us to verify
     * if the current user is the main
     * technical in the order work
     */
    public void  checkIfIsMainTechnical(final WorkOrder workOrder){
        mRv.setVisibility(View.INVISIBLE);
        mPgBar.setVisibility(View.VISIBLE);

        String osql = "SELECT Id FROM Tecnicos_por_Orden_de_Trabajo__c WHERE " +
                "Tecnico__c = '"+Utility.getRestClient().getClientInfo().userId+"' AND " +
                "Work_Order__c = '"+workOrder.getId()+"'";

        //Utility.logLargeString(osql);

        ApiManager.getInstance().getJSONObject(this, osql, new ApiManager.OnObjectListener() {
            @Override
            public void onObject(boolean success, JSONObject jsonObject, String errorMessage) {
                /*Here we hide the progress bar*/
                mPgBar.setVisibility(View.GONE);

                if(success){
                    try {
                        //Here we start the check flow
                        mRv.setVisibility(View.VISIBLE);

                        CheckPointData checkPointData = new CheckPointData();
                        checkPointData.setId(workOrder.getId());
                        checkPointData.setLatitude(workOrder.getLatitude());
                        checkPointData.setLongitude(workOrder.getLongitude());
                        checkPointData.setContactId(workOrder.getContactId());
                        checkPointData.setCheckPointType(3);
                        checkPointData.setName(workOrder.getContactName());

                        if(workOrder.getCountry()!=null){
                            checkPointData.setName(workOrder.getWorkOrderNumber()+"-"+workOrder.getCountry());
                        }else{
                            checkPointData.setName(workOrder.getWorkOrderNumber());
                        }

                        /*Here we check if the technical is te main*/
                        if(jsonObject.getInt("totalSize") == 1){
                            checkPointData.setIsMainTechnical(true);
                            checkPointData.setMainTechnicalId(Utility.getRestClient().getClientInfo().userId);
                        }else{
                            checkPointData.setIsMainTechnical(false);
                        }

                        /*Here we notify the result*/
                        Intent intent= new Intent();
                        intent.setAction(ACTION_SEARCH_RESULT);
                        intent.putExtra(MyOrderWorksActivity.ARG_CHECK_POINT_DATA,checkPointData);
                        sendBroadcast(intent);

                        finish();

                    } catch (JSONException e) {
                        mRv.setVisibility(View.VISIBLE);
                        showMessage(R.string.technical_main_check_fail);
                    }
                }else{
                    mRv.setVisibility(View.VISIBLE);
                    showMessage(R.string.technical_main_check_fail);
                }
            }
        });
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


    /**
     * This method show a single message
     */
    public void showMessage(String message){
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
