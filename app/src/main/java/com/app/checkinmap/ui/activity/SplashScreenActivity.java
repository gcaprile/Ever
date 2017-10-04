package com.app.checkinmap.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.app.checkinmap.R;
import com.app.checkinmap.util.Utility;
import com.salesforce.androidsdk.accounts.UserAccount;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.ui.SalesforceActivity;

public class SplashScreenActivity extends SalesforceActivity {
    public static final String TAG = SplashScreenActivity.class.getName();

    public static final int SPLASH_SCREEN_TIME= 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }

    @Override
    public void onResume() {
        findViewById(R.id.root).setVisibility(View.INVISIBLE);
        super.onResume();
    }

    @Override
    public void onResume(RestClient client) {
        findViewById(R.id.root).setVisibility(View.VISIBLE);
        Utility.setRestClient(client);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(DashBoardActivity.getIntent(getApplication()));
                finish();
            }
        },SPLASH_SCREEN_TIME);
    }
}
