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
import android.widget.Toast;

import com.app.checkinmap.R;
import com.app.checkinmap.model.Account;
import com.app.checkinmap.model.Record;
import com.app.checkinmap.ui.adapter.AccountAdapterList;
import com.app.checkinmap.ui.adapter.HistoryAdapterList;
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

public class MyAccountsActivity extends AppCompatActivity implements AccountAdapterList.OnItemClickListener {

    @BindView(R.id.rcv_accounts)
    RecyclerView mRv;

    @BindView(R.id.progress_bar)
    ProgressBar mPgBar;

    @BindView(R.id.text_view_message)
    TextView mTxvMessage;

    private AccountAdapterList mAdapter;

    /**
     * This method help us to get a single
     * intent in order to get a my account activity
     * instance
     */
    public static Intent getIntent(Context context){
        Intent intent = new Intent(context,MyAccountsActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_accounts);

        ButterKnife.bind(this);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.my_accounts);
        }

        mRv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRv.setLayoutManager(layoutManager);

        /*Here we get the accounts from the sales force*/
        getAccountFromSalesForce();
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

                        mAdapter = new AccountAdapterList(getApplicationContext(),accountList);

                        loadListData();

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
    public void loadListData(){
        mAdapter.setOnItemClickListener(this);
        mRv.setAdapter(mAdapter);
        mRv.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(Account account) {
        startActivity(AccountDetailActivity.getIntent(getApplicationContext(),account));
    }
}
