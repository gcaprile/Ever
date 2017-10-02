package com.app.checkinmap.db;

import com.app.checkinmap.model.CheckPointLocation;
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

    public List<CheckPointLocation> getCheckPointLocationList(){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<CheckPointLocation> query = realm.where(CheckPointLocation.class).findAll();
        List<CheckPointLocation> checkPointLocationList = null;
        if(query != null){
            checkPointLocationList = realm.copyFromRealm(query);
        }
        realm.close();
        return checkPointLocationList;
    }
}
