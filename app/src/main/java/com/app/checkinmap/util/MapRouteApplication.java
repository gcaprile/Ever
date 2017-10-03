package com.app.checkinmap.util;

import android.app.Application;

import com.app.checkinmap.ui.activity.SplashScreenActivity;
import com.salesforce.androidsdk.smartsync.app.SmartSyncSDKManager;

import io.realm.Realm;

/**
 * This class help us to init
 * the ORM in the application
 */

public class MapRouteApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SmartSyncSDKManager.initNative(getApplicationContext(), new NativeKeyImpl(), SplashScreenActivity.class);
        Realm.init(this);
    }

}
