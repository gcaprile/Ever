package com.app.checkinmap.ui.activity;

import android.content.Context;
import android.content.Intent;
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
import com.app.checkinmap.model.Account;
import com.app.checkinmap.model.AccountAddress;
import com.app.checkinmap.model.Lead;
import com.app.checkinmap.model.WorkOrder;
import com.app.checkinmap.ui.adapter.LeadAdapterList;
import com.app.checkinmap.ui.adapter.WorkOrderAdapterList;
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

import static com.app.checkinmap.ui.activity.AccountDetailActivity.REQUEST_ADDRESS_SELECTION;
import static com.app.checkinmap.ui.activity.CheckPointMapActivity.REQUEST_CHECK_IN;

public class MyOrderWorksActivity extends AppCompatActivity implements WorkOrderAdapterList.OnItemClickListener{
    public static final int REQUEST_WORK_ORDER_SELECTION = 17;

    @BindView(R.id.rcv_work_orders)
    RecyclerView mRv;

    @BindView(R.id.progress_bar)
    ProgressBar mPgBar;

    @BindView(R.id.text_view_message)
    TextView mTxvMessage;

    private WorkOrderAdapterList mAdapter;


    /**
     * This method help us to get a single
     * intent in order to get a my order work
     * instance
     */
    public static Intent getIntent(Context context){
        Intent intent = new Intent(context,MyOrderWorksActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_works);

        ButterKnife.bind(this);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.my_work_orders);
        }

        mRv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRv.setLayoutManager(layoutManager);

        getWorkOrdersFromSalesForce();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CHECK_IN:
                if(resultCode == RESULT_OK){
                    setResult(RESULT_OK);
                    finish();
                }
                break;
        }
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
            mAdapter = new WorkOrderAdapterList(workOrderList);
            mAdapter.setOnItemClickListener(this);
            mRv.setAdapter(mAdapter);
            mRv.setVisibility(View.VISIBLE);
        }else{
            mTxvMessage.setText(R.string.no_work_orders_to_show);
            mTxvMessage.setVisibility(View.VISIBLE);
        }
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

        String osql = "SELECT Id__c, Name__c, Principal__c, Work_Order__c FROM Tecnicos_por_Orden_de_Trabajo__c WHERE Tecnico__c = '0056A000000r6UsQAI' AND Work_Order__c = '0WO6A000000lpymWAA'";

        Utility.logLargeString(osql);

        ApiManager.getInstance().getJSONObject(this, osql, new ApiManager.OnObjectListener() {
            @Override
            public void onObject(boolean success, JSONObject jsonObject, String errorMessage) {
                /*Here we hide the progress bar*/
                mPgBar.setVisibility(View.GONE);

                /*Here we create the address object*/

                AccountAddress leadAddress= new AccountAddress();
                leadAddress.setLatitude(workOrder.getLatitude());
                leadAddress.setLongitude(workOrder.getLongitude());
                leadAddress.setName(workOrder.getWorkOrderNumber());
                leadAddress.setWorkOrderId(workOrder.getId());
                leadAddress.setIsPrincipal(true);

                //Here we start the check flow
                startActivityForResult(CheckPointMapActivity.getIntent(getApplicationContext(),3,workOrder.getWorkOrderNumber(),leadAddress),
                        REQUEST_CHECK_IN);

                /*if(success){
                    Utility.logLargeString(jsonObject.toString());
                }else{
                    mRv.setVisibility(View.VISIBLE);
                    //showMessage(errorMessage);
                    showMessage(R.string.technical_main_check_fail);
                }*/
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
