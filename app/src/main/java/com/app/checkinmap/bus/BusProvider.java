package com.app.checkinmap.bus;


import com.squareup.otto.Bus;

/**
 * Bus Provider to send and receive events using this instance.
 */
public class BusProvider {

    private static Bus mBus;

    public static Bus getInstance(){
        if(mBus == null){
            mBus = new Bus();
        }

        return mBus;
    }
}
