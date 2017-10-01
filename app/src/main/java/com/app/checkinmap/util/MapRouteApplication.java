package com.app.checkinmap.util;

import io.realm.Realm;

/**
 * This class help us to init
 * the ORM in the application
 */

public class MapRouteApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
       Realm.init(this);
    }
}
