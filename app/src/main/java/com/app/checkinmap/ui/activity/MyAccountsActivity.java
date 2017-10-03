package com.app.checkinmap.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.checkinmap.R;
import com.app.checkinmap.model.Record;
import com.app.checkinmap.ui.adapter.AccountAdapterList;
import com.app.checkinmap.ui.adapter.HistoryAdapterList;
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

public class MyAccountsActivity extends AppCompatActivity {

    @BindView(R.id.rcv_accounts)
    RecyclerView mRv;

    @BindView(R.id.progress_bar)
    ProgressBar mPgBar;

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


        ApiManager.getInstance().getJSONObject(this, "SELECT Name FROM Account", new ApiManager.OnObjectListener() {
            @Override
            public void onObject(final boolean success,final JSONObject jsonObject, String errorMessage) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPgBar.setVisibility(View.GONE);
                        if(success){
                            Type listType = new TypeToken<List<Record>>() {}.getType();
                            try {
                                final List<Record> recordList = new Gson().fromJson(jsonObject.getJSONArray("records").toString(), listType);

                                AccountAdapterList adapter = new AccountAdapterList(recordList);
                                mRv.setAdapter(adapter);
                                mRv.setVisibility(View.VISIBLE);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(),"Error al obtener las cuentas",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(),"Error en la peticion",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
