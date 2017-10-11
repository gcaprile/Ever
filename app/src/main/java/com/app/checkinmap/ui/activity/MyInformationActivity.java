package com.app.checkinmap.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.app.checkinmap.R;
import com.app.checkinmap.util.Utility;
import com.salesforce.androidsdk.rest.RestClient;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyInformationActivity extends AppCompatActivity {
    @BindView(R.id.text_view_display_name)
    TextView mTxvDisplayName;

    @BindView(R.id.text_view_email)
    TextView mTxvEmail;

    @BindView(R.id.text_view_user_name)
    TextView mTxvUserName;


    @BindView(R.id.text_view_account_name)
    TextView mTxvAccountName;

    /**
     * This method help us to get a single
     * intent in order to get a history activity
     * instance
     */
    public static Intent getIntent(Context context){
        Intent intent = new Intent(context,MyInformationActivity.class);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_information);

        ButterKnife.bind(this);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.my_information);
        }

        RestClient.ClientInfo ci = Utility.getRestClient().getClientInfo();
        mTxvDisplayName.setText(ci.displayName);
        mTxvEmail.setText(ci.email);
        mTxvUserName.setText(ci.username);
        mTxvAccountName.setText(ci.accountName);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
