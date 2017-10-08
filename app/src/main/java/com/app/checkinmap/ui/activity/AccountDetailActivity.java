package com.app.checkinmap.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.checkinmap.R;
import com.app.checkinmap.model.Account;
import com.app.checkinmap.model.AccountAddress;
import com.app.checkinmap.ui.adapter.AddressAdapterList;
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

public class AccountDetailActivity extends AppCompatActivity implements AddressAdapterList.OnItemClickListener{

    public static final int REQUEST_ADDRESS_SELECTION = 78;

     public static final String ARG_SELECTION="selection";

    @BindView(R.id.linear_layout_main_content)
    LinearLayout mLnlMainContent;

    @BindView(R.id.text_view_account_name)
    TextView mTxvAccountName;

    @BindView(R.id.text_view_total_address)
    TextView mTxvAddressNumber;

    @BindView(R.id.text_view_indication)
    TextView mTxvIndication;

    @BindView(R.id.recycler_view_address)
    RecyclerView mRcvAddress;

    @BindView(R.id.progress_bar)
    ProgressBar mPgb;

    @BindView(R.id.text_view_message)
    TextView mTxvMessage;

    private Account            mAccount;
    private AddressAdapterList mAdapter;

    /**
     * This method help us to get a single instance
     * for account detail activity
     */
    public static Intent getIntent(Context context,Account account){
        Intent intent = new Intent(context,AccountDetailActivity.class);
        intent.putExtra(ARG_SELECTION,account);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);

        ButterKnife.bind(this);

        mAccount = getIntent().getExtras().getParcelable(ARG_SELECTION);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(mAccount.getName());
        }

        /*Here we make the request to sales force*/
        getAccountAddressFromSalesForce();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CHECK_IN:
                if(resultCode ==  RESULT_OK){
                    setResult(RESULT_OK);
                    finish();
                }
                break;
        }
    }

    /**
     * This method help us to get all the address´s accounts from
     * sales force
     */
    public void getAccountAddressFromSalesForce(){
        String osql = "SELECT Id, Name, Principal__c, Direccion__c, Ciudad__c, Estado_o_Provincia__c, Pais__c, " +
                "Codigo_Postal__c, Coordenadas__Latitude__s, Coordenadas__Longitude__s, Coordenadas__c, Activa__c, " +
                "Cuenta__c FROM Direcciones__c WHERE Cuenta__c = '"+mAccount.getId()+"' ORDER BY Principal__c DESC";

        ApiManager.getInstance().getJSONObject(this, osql, new ApiManager.OnObjectListener() {
            @Override
            public void onObject(boolean success, JSONObject jsonObject, String errorMessage) {
                /*Here we hide the progress bar*/
                mPgb.setVisibility(View.GONE);
                if(success){
                    Utility.logLargeString(jsonObject.toString());
                   try {
                        Type listType = new TypeToken<List<AccountAddress>>() {}.getType();
                        List<AccountAddress> accountAddressList = new Gson().fromJson(jsonObject.getJSONArray("records").toString(), listType);

                        loadListData(accountAddressList);

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
    public void loadListData(List<AccountAddress> addressList){
        /*Here we set the header information*/
        mTxvAccountName.setText(mAccount.getName());
        mTxvAddressNumber.setText("El cliente tiene "+addressList.size()+" direcciones");

        if(addressList.size()>0){
             /*Here we set the content in the list*/
            mAdapter = new AddressAdapterList(addressList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mRcvAddress.setLayoutManager(layoutManager);
            mRcvAddress.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(this);

        }else{
            mTxvMessage.setText(R.string.no_address_to_show);
            mTxvIndication.setVisibility(View.INVISIBLE);
            mTxvMessage.setVisibility(View.VISIBLE);
        }
        mLnlMainContent.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(AccountAddress accountAddress) {
        if(PreferenceManager.getInstance(this).isInRoute()){
            startActivityForResult(CheckPointMapActivity.getIntent(getApplicationContext(),mAccount.getName(),accountAddress),
                    REQUEST_CHECK_IN);
        }else{
            new AlertDialog.Builder(this)
                    .setTitle(R.string.app_name)
                    .setMessage(R.string.you_should_start_the_route)
                    .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    }).setCancelable(false).show();
        }
    }
}
