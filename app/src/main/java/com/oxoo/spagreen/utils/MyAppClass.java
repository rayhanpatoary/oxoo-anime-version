package com.oxoo.spagreen.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.onesignal.OneSignal;

public class MyAppClass extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext=this;

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();


        SharedPreferences preferences=getSharedPreferences("push",MODE_PRIVATE);
        if (preferences.getBoolean("status",true)){
            OneSignal.setSubscription(true);
        }else {
            OneSignal.setSubscription(false);
        }


    }

    public static Context getContext(){
        return mContext;
    }
}
