package com.app.checkinmap.ui.activity;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SearchEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.app.checkinmap.R;
import com.app.checkinmap.model.Account;
import com.app.checkinmap.model.AccountAddress;
import com.app.checkinmap.model.CheckPointData;
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
import static com.app.checkinmap.ui.activity.SearchableWorkOrderActivity.ACTION_SEARCH_RESULT;

public class MyOrderWorksActivity extends AppCompatActivity implements WorkOrderAdapterList.OnItemClickListener{
    public static final int REQUEST_WORK_ORDER_SELECTION = 17;
    public static final String ARG_CHECK_POINT_DATA="account_address_selected";

    @BindView(R.id.rcv_work_orders)
    RecyclerView mRv;

    @BindView(R.id.progress_bar)
    ProgressBar mPgBar;

    @BindView(R.id.text_view_message)
    TextView mTxvMessage;

    private WorkOrderAdapterList mAdapter;

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

        registerReceiver(mSearchResultReceiver, new IntentFilter(ACTION_SEARCH_RESULT));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CHECK_IN:
                if(resultCode == RESULT_OK){
                    setResult(RESULT_OK,data);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mSearchResultReceiver);
    }

    /**
     * This method help us to get all the accounts from
     * sales force
     */
    public void getWorkOrdersFromSalesForce(){

       /*String osql = "SELECT Id, WorkOrderNumber, AccountId, ContactId, Country, " +
                "Latitude, Longitude, Description, StartDate,EndDate, Status FROM WorkOrder";*/

       String osql = "SELECT Id, WorkOrderNumber, Country, Latitude, Longitude, " +
               "Cuenta_del__c, Contacto__c, status, Detalle_Direccion__c, AccountId, ContactID, Direccion_Visita__c " +
               "FROM WorkOrder ORDER BY WorkOrderNumber";

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
        mPgBar.setVisibility(View.VISIBLE);

        String osql = "SELECT Id FROM Tecnicos_por_Orden_de_Trabajo__c WHERE " +
                "Tecnico__c = '"+Utility.getRestClient().getClientInfo().userId+"' AND " +
                "Work_Order__c = '"+workOrder.getId()+"'";

       // Utility.logLargeString(osql);

        ApiManager.getInstance().getJSONObject(this, osql, new ApiManager.OnObjectListener() {
            @Override
            public void onObject(boolean success, JSONObject jsonObject, String errorMessage) {
                /*Here we hide the progress bar*/
                mPgBar.setVisibility(View.GONE);
                if(success){
                    try {

                        CheckPointData checkPointData = new CheckPointData();
                        checkPointData.setId(workOrder.getId());
                        checkPointData.setLatitude(workOrder.getLatitude());
                        checkPointData.setLongitude(workOrder.getLongitude());
                        checkPointData.setContactId(workOrder.getContactId());
                        checkPointData.setName(workOrder.getContactName());
                        if(workOrder.getAddressDetail()!=null){
                            checkPointData.setAddress(workOrder.getAddressDetail());
                        }else{
                            checkPointData.setAddress("");
                        }
                        checkPointData.setCheckPointType(3);


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

                        //Here we start the check flow
                        mRv.setVisibility(View.VISIBLE);
                        startCheckPointFlow(checkPointData);

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

    /**
     * Here we start the check point flow
     */
    public void startCheckPointFlow(CheckPointData checkPointData){


        startActivityForResult(CheckPointMapActivity.getIntent(getApplicationContext(),checkPointData),
                REQUEST_CHECK_IN);
    }
}
