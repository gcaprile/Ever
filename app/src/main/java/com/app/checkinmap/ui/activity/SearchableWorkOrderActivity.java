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

        String osql = "SELECT Id, Tecnico__c, Principal__c, Work_Order__c, work_order__r.WorkOrderNumber, work_order__r.Direccion_Visita__r.id, work_order__r.Cuenta_del__c, work_order__r.Contacto__c, work_order__r.status, work_order__r.direccion_visita__r.Direccion__c,work_order__r.direccion_visita__r.Ciudad__c, work_order__r.direccion_visita__r.estado_o_provincia__c, work_order__r.direccion_visita__r.pais__c," +
                " work_order__r.direccion_visita__r.coordenadas__c, work_order__r.AccountId, work_order__r.ContactID" +
                " FROM Tecnicos_por_Orden_de_Trabajo__c" +
                " WHERE  Tecnico__c = '"+Utility.getRestClient().getClientInfo().userId+"'" +
                " AND (work_order__r.status = 'Open' OR work_order__r.status = 'In Process')";


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
            if(workOrder.getWorkOrderDetail().getWorkOrderNumber().toLowerCase().contains(mQuery.toLowerCase())){
                workOrderList.add(workOrder);
            }else{
                if(workOrder.getWorkOrderDetail().getContactAccountName()!=null){
                    if(workOrder.getWorkOrderDetail().getContactAccountName().toLowerCase().contains(mQuery.toLowerCase())){
                        workOrderList.add(workOrder);
                    }else{
                        if(workOrder.getWorkOrderDetail().getContactName()!=null){
                            if(workOrder.getWorkOrderDetail().getContactName().toLowerCase().contains(mQuery.toLowerCase())){
                                workOrderList.add(workOrder);
                            }else{
                                if(workOrder.getWorkOrderDetail().getWorkOrderAddress()!=null){
                                    if(workOrder.getWorkOrderDetail().getWorkOrderAddress().getAddress().toLowerCase().contains(mQuery.toLowerCase())){
                                        workOrderList.add(workOrder);
                                    }
                                }
                            }
                        }else{
                            if(workOrder.getWorkOrderDetail().getWorkOrderAddress()!=null){
                                if(workOrder.getWorkOrderDetail().getWorkOrderAddress().getAddress().toLowerCase().contains(mQuery.toLowerCase())){
                                    workOrderList.add(workOrder);
                                }
                            }
                        }
                    }
                }else{
                    if(workOrder.getWorkOrderDetail().getContactName()!=null){
                        if(workOrder.getWorkOrderDetail().getContactName().toLowerCase().contains(mQuery.toLowerCase())){
                            workOrderList.add(workOrder);
                        }else{
                            if(workOrder.getWorkOrderDetail().getWorkOrderAddress()!=null){
                                if(workOrder.getWorkOrderDetail().getWorkOrderAddress().getAddress().toLowerCase().contains(mQuery.toLowerCase())){
                                    workOrderList.add(workOrder);
                                }
                            }
                        }
                    }else{
                        if(workOrder.getWorkOrderDetail().getWorkOrderAddress()!=null){
                            if(workOrder.getWorkOrderDetail().getWorkOrderAddress().getAddress().toLowerCase().contains(mQuery.toLowerCase())){
                                workOrderList.add(workOrder);
                            }
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

            CheckPointData checkPointData = new CheckPointData();
            checkPointData.setId(workOrder.getWorkOrderId());
            checkPointData.setContactId(workOrder.getWorkOrderDetail().getContactId());
            checkPointData.setContactName(workOrder.getWorkOrderDetail().getContactName());
            checkPointData.setIsMainTechnical(workOrder.isIsPrincipal());
            checkPointData.setMainTechnicalId(workOrder.getTechnicalId());
            checkPointData.setCheckPointType(3);
            if(workOrder.getWorkOrderDetail().getWorkOrderAddress()!=null){
                checkPointData.setAddress(workOrder.getWorkOrderDetail().getWorkOrderAddress().getAddress());
            }else{
                checkPointData.setAddress("");
            }
            if(workOrder.getWorkOrderDetail().getWorkOrderAddress()!=null){
                checkPointData.setName(workOrder.getWorkOrderDetail().getWorkOrderNumber()+"-"+workOrder.getWorkOrderDetail().getWorkOrderAddress().getCountry());
                if(workOrder.getWorkOrderDetail().getWorkOrderAddress().getCoordinates()!=null){
                    checkPointData.setLatitude(workOrder.getWorkOrderDetail().getWorkOrderAddress().getCoordinates().getLatitude());
                    checkPointData.setLongitude(workOrder.getWorkOrderDetail().getWorkOrderAddress().getCoordinates().getLongitude());
                }else{
                    checkPointData.setLatitude(0);
                    checkPointData.setLongitude(0);
                }
            }else{
                checkPointData.setName(workOrder.getWorkOrderDetail().getWorkOrderNumber());
            }

                        /*Here we notify the result*/
            Intent intent= new Intent();
            intent.setAction(ACTION_SEARCH_RESULT);
            intent.putExtra(MyOrderWorksActivity.ARG_CHECK_POINT_DATA,checkPointData);
            sendBroadcast(intent);

            finish();
        }else{
            showMessage(R.string.you_should_start_the_route);
        }
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
