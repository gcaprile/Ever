package com.app.checkinmap.ui.activity;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.checkinmap.R;
import com.app.checkinmap.model.Account;
import com.app.checkinmap.model.AccountAddress;
import com.app.checkinmap.ui.adapter.AccountAdapterList;
import com.app.checkinmap.util.ApiManager;
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

import static com.app.checkinmap.ui.activity.AccountDetailActivity.REQUEST_ADDRESS_SELECTION;
import static com.app.checkinmap.ui.activity.SearchableLeadsActivity.ACTION_SEARCH_RESULT;

public class SearchableAccountsActivity extends AppCompatActivity implements AccountAdapterList.OnItemClickListener {
    public static final int REQUEST_ACCOUNT_SELECTION = 77;
    public static final String ACTION_SEARCH_RESULT= "com.app.checkinmap.SEARCH_RESULT";

    @BindView(R.id.rcv_accounts)
    RecyclerView mRv;

    @BindView(R.id.progress_bar)
    ProgressBar mPgBar;

    @BindView(R.id.text_view_message)
    TextView mTxvMessage;

    private AccountAdapterList mAdapter;
    private String mQuery;


    /**
     * This method help us to get a single
     * intent in order to get a my account activity
     * instance
     */
    public static Intent getIntent(Context context){
        Intent intent = new Intent(context,SearchableAccountsActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_accounts);

        ButterKnife.bind(this);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.text_result);
        }

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {

            mQuery = intent.getStringExtra(SearchManager.QUERY);

            Log.d("Busqueda",mQuery);

            mRv.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mRv.setLayoutManager(layoutManager);

            /*Here we get the accounts from the sales force*/
            getAccountFromSalesForce();

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
            case REQUEST_ADDRESS_SELECTION:
                if(resultCode == RESULT_OK){
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
    public void getAccountFromSalesForce(){
        String osql = "SELECT Id, Name, Phone, Emasal_Address__c, Pais__c, Description FROM Account order by Id";
        ApiManager.getInstance().getJSONObject(this, osql, new ApiManager.OnObjectListener() {
            @Override
            public void onObject(boolean success, JSONObject jsonObject, String errorMessage) {
                /*Here we hide the progress bar*/
                mPgBar.setVisibility(View.GONE);
                if(success){
                    Utility.logLargeString(jsonObject.toString());
                    try {
                        Type listType = new TypeToken<List<Account>>() {}.getType();
                        List<Account> accountList = new Gson().fromJson(jsonObject.getJSONArray("records").toString(), listType);

                        loadListData(accountList);

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
    public void loadListData(List<Account> accountList){
        if(accountList.size()>0){
            List<Account> list = getFilterAccounts(accountList);

            if(list.size()>0){
                mAdapter = new AccountAdapterList(getApplicationContext(),list);
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
     * This method help us to get all the
     * filter accounts
     */
    public List<Account> getFilterAccounts(List<Account> accountList){
        List<Account> list = new ArrayList<>();
        for(Account account : accountList){

            if(account.getName()!=null){
                if(account.getName().toLowerCase().contains(mQuery.toLowerCase())){
                    list.add(account);
                }else{
                    if(account.getAddress()!=null){
                        if(account.getAddress().toLowerCase().contains(mQuery.toLowerCase())){
                            list.add(account);
                        }
                    }
                }
            }else{
                if(account.getAddress()!=null){
                    if(account.getAddress().toLowerCase().contains(mQuery.toLowerCase())){
                        list.add(account);
                    }
                }
            }
        }

        return list;
    }

    @Override
    public void onItemClick(Account account) {
        //startActivityForResult(AccountDetailActivity.getIntent(getApplicationContext(),account),REQUEST_ADDRESS_SELECTION);
         /*Here we notify the result*/
        Intent intent= new Intent();
        intent.setAction(ACTION_SEARCH_RESULT);
        intent.putExtra(MyAccountsActivity.ARG_ADDRESS_DATA,account);
        sendBroadcast(intent);

        finish();
    }
}
