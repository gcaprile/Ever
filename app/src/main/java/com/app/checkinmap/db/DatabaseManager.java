package com.app.checkinmap.db;

import com.app.checkinmap.model.UserLocation;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class DatabaseManager {

    private static DatabaseManager mInstance;

    public static DatabaseManager getInstance(){
        if(mInstance == null){
            mInstance = new DatabaseManager();
        }

        return mInstance;
    }

    public List<UserLocation> getUserLocationList(){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<UserLocation> query = realm.where(UserLocation.class).findAll();
        List<UserLocation> userLocationList = null;
        if(query != null){
            userLocationList = realm.copyFromRealm(query);
        }
        realm.close();
        return userLocationList;
    }
}
