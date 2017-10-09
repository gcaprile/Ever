package com.app.checkinmap.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.checkinmap.R;
import com.app.checkinmap.util.ApiManager;
import com.app.checkinmap.util.Utility;
import com.salesforce.androidsdk.accounts.UserAccount;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.ui.SalesforceActivity;

import org.json.JSONException;
import org.json.JSONObject;

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

        getUserProfileAndStar();
    }

    /**
     * This method help us to get the user profile
     */
    public void getUserProfileAndStar(){
        RestClient.ClientInfo clientInfo = Utility.getRestClient().getClientInfo();

        String osql ="SELECT User.id, User.Email, User.FirstName, User.LastName, User.profile.id, User.profile.name, User.Username, " +
                "User.Country, User.IsActive FROM User, User.profile WHERE User.id = '"+clientInfo.userId+"'";

        ApiManager.getInstance().getJSONObject(this, osql, new ApiManager.OnObjectListener() {
            @Override
            public void onObject(boolean success, JSONObject jsonObject, String errorMessage) {
                if(success){
                    Utility.logLargeString(jsonObject.toString());
                    try {
                        Utility.setUserProfileId(jsonObject.getJSONArray("records").getJSONObject(0).getJSONObject("Profile").getString("Id"));
                        Utility.setUserProfileName(jsonObject.getJSONArray("records").getJSONObject(0).getJSONObject("Profile").getString("Name"));
                        Utility.setUserCountry(jsonObject.getJSONArray("records").getJSONObject(0).getString("Country"));
                        startActivity(DashBoardActivity.getIntent(getApplicationContext()));
                        finish();
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(),R.string.no_user_profile_id,Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),R.string.no_user_profile,Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
