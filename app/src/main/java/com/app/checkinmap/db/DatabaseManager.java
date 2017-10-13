package com.app.checkinmap.db;

import android.util.Log;

import com.app.checkinmap.model.CheckPointData;
import com.app.checkinmap.model.CheckPointLocation;
import com.app.checkinmap.model.Route;
import com.app.checkinmap.model.UserLocation;
import com.app.checkinmap.util.PreferenceManager;

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

    /**
     * This method get all the user location saved
     * in local storage
     */
    public List<UserLocation> getUserLocationList(long routeId){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<UserLocation> query = realm.where(UserLocation.class).equalTo("routeId",routeId).findAll();
        List<UserLocation> userLocationList = null;
        if(query != null){
            userLocationList = realm.copyFromRealm(query);
        }
        realm.close();
        return userLocationList;
    }

    /**
     * This method help us to get all the check point
     * from data base from a specific route
     */
    public List<CheckPointLocation> getCheckPointLocationList(long routeId){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<CheckPointLocation> query = realm.where(CheckPointLocation.class).contains("routeId",String.valueOf(routeId) ).findAll();
        List<CheckPointLocation> checkPointLocationList = null;
        if(query != null){
            checkPointLocationList = realm.copyFromRealm(query);
        }
        realm.close();
        return checkPointLocationList;
    }


    /**
     * This method help us to get the correlative
     * to create the route name
     */
    public int getCorrelativeRoute(String date){
        int correlative = 1;

        Realm realm = Realm.getDefaultInstance();
        RealmResults<Route> query = realm.where(Route.class).contains("startDate",date).findAll();
        List<Route> routeList = null;
        if(query != null){
            routeList = realm.copyFromRealm(query);
            if(routeList.size()>0){
                correlative = routeList.size() +1;
                Log.d("Correlativo creado",String.valueOf(correlative));
            }
        }
        realm.close();

        return correlative;
    }

    /**
     * This method help us to get the correlative
     * to create the check point name
     */
    public int getCorrelativeCheckPoint(long routeId){
        int correlative = 1;

        Realm realm = Realm.getDefaultInstance();
        RealmResults<CheckPointLocation> query = realm.where(CheckPointLocation.class).contains("routeId",String.valueOf(routeId)).findAll();
        List<CheckPointLocation> checkPointList = null;
        if(query != null){
            checkPointList = realm.copyFromRealm(query);
            if(checkPointList.size()>0){
                correlative = checkPointList.size() +1;
                Log.d("Check point c ",String.valueOf(correlative));
            }
        }
        realm.close();

        return correlative;
    }


    /**
     * This method help us to get the start date for the
     * travel in a visit
     */
    public String getTravelStartDate(long routeId){
        String startDate = "";

        Realm realm = Realm.getDefaultInstance();
        RealmResults<CheckPointLocation> query = realm.where(CheckPointLocation.class).contains("routeId",String.valueOf(routeId)).findAll();
        List<CheckPointLocation> checkPointList = null;
        if(query != null){
            checkPointList = realm.copyFromRealm(query);
            if(checkPointList.size()>0){
                startDate = checkPointList.get(checkPointList.size()-1).getCheckOutDate();
            }else{
                Route route = realm.where(Route.class).equalTo("id",routeId).findFirst();
                if(route!=null){
                    startDate = route.getStartDate();
                }
            }
        }
        realm.close();

        return startDate;
    }


    /**
     * This method help us to get all the check point
     * from data base from a specific route
     */
    public CheckPointLocation getCheckPointLocation(long id){
        CheckPointLocation checkPointLocation = new CheckPointLocation();
        Realm realm = Realm.getDefaultInstance();
        CheckPointLocation checkPointLocationQuery = realm.where(CheckPointLocation.class).equalTo("id",id ).findFirst();
        if(checkPointLocationQuery!=null){
            checkPointLocation = realm.copyFromRealm(checkPointLocationQuery);
        }
        realm.close();
        return checkPointLocation;
    }

    /**
     * This method help us to get all the check point
     * from data base from a specific route
     */
    public String getRouteName(long id){
        Route route = new Route();
        Realm realm = Realm.getDefaultInstance();
        Route routeQuery = realm.where(Route.class).equalTo("id",id ).findFirst();
        if(routeQuery!=null){
            route = realm.copyFromRealm(routeQuery);
        }
        realm.close();
        return route.getName();
    }
}
