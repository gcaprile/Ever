package com.app.checkinmap.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.app.checkinmap.R;
import com.app.checkinmap.ui.adapter.AddressAdapterList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountDetailActivity extends AppCompatActivity {

     public static final String ARG_SELECTION="selection";

    @BindView(R.id.text_view_account_name)
    TextView mTxvAccounName;

    @BindView(R.id.text_view_total_address)
    TextView mTxvAddressNumber;

    @BindView(R.id.recycler_view_address)
    RecyclerView mRcvAddress;

    /**
     * This method help us to get a single instance
     * for account detail activity
     */
    public static Intent getIntent(Context context,String selection){
        Intent intent = new Intent(context,AccountDetailActivity.class);
        intent.putExtra(ARG_SELECTION,selection);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);

        ButterKnife.bind(this);

        String accountName = getIntent().getExtras().getString(ARG_SELECTION);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setTitle(accountName);
        }

        mTxvAccounName.setText(accountName);
        mTxvAddressNumber.setText("El cliente tiene 10 direcciones");
        AddressAdapterList adapterList = new AddressAdapterList();
        adapterList.setOnItemClickListener(new AddressAdapterList.OnItemClickListener() {
            @Override
            public void onItemClick(String selection) {
                startActivity(MapRouteActivity.getIntent(getApplicationContext(),selection));
                finish();
            }
        });
        mRcvAddress.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRcvAddress.setLayoutManager(layoutManager);
        mRcvAddress.setAdapter(adapterList);

    }
}
